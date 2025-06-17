import os
import requests
from datetime import datetime
from dotenv import load_dotenv
from langchain.callbacks.tracers import ConsoleCallbackHandler
from langchain_core.tools import tool
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.prebuilt import create_react_agent

load_dotenv()

api_key = os.getenv("GEMINI_API_KEY")
if not api_key:
    raise ValueError("GEMINI_API_KEY not found in .env file")

llm = ChatGoogleGenerativeAI(
    model="gemini-2.0-flash",
    google_api_key=api_key,
    temperature=0
)

@tool
def get_weather(location: str) -> str:
    """
    Fetches the current weather for the given location using wttr.in (no API key required).
    """
    url = f"https://wttr.in/{location}?format=3"
    try:
        response = requests.get(url)
        if response.status_code == 200:
            return response.text.strip()
        else:
            return f"Could not retrieve weather data for {location}."
    except Exception as e:
        return f"Error: {e}"

@tool
def get_time(location: str) -> str:
    """
    Returns the current local time for a given location (city or country) using timeapi.io (no API key required).
    """
    try:
        # Geocode to lat/lon
        geocode_url = "https://nominatim.openstreetmap.org/search"
        geocode_params = {"q": location, "format": "json", "limit": 1}
        geo_resp = requests.get(geocode_url, params=geocode_params, headers={"User-Agent": "timezone-query"})
        geo_data = geo_resp.json()
        if not geo_data:
            return "Could not find that location."
        lat = geo_data[0]["lat"]
        lon = geo_data[0]["lon"]
        # Get timezone from timeapi.io
        tz_url = f"https://timeapi.io/api/TimeZone/coordinate?latitude={lat}&longitude={lon}"
        tz_resp = requests.get(tz_url)
        if tz_resp.status_code == 200:
            tz_data = tz_resp.json()
            tz = tz_data.get("timeZone")
        else:
            return "Sorry, I am unable to retrieve the current time."
        if not tz:
            return "Sorry, I am unable to retrieve the current time."
        # Get current time from timeapi.io
        url = f"https://timeapi.io/api/Time/current/zone?timeZone={tz}"
        resp = requests.get(url)
        if resp.status_code == 200:
            data = resp.json()
            dt = datetime.fromisoformat(data['dateTime'].split('.')[0])
            formatted_time = dt.strftime("%H:%M")
            return f"The current time in {location.title()} is {formatted_time}."
        return "Sorry, I am unable to retrieve the current time."
    except Exception as e:
        return f"Error during time lookup: {e}"

@tool
def get_timezone(location: str) -> str:
    """
    Returns the timezone name for a given location using timeapi.io and Nominatim.
    """
    geocode_url = "https://nominatim.openstreetmap.org/search"
    geocode_params = {"q": location, "format": "json", "limit": 1}
    try:
        geo_resp = requests.get(geocode_url, params=geocode_params, headers={"User-Agent": "timezone-query"})
        geo_data = geo_resp.json()
        if not geo_data:
            return "Could not find that location."
        lat = geo_data[0]["lat"]
        lon = geo_data[0]["lon"]
    except Exception as e:
        return f"Error during geocoding: {e}"

    try:
        tz_url = f"https://timeapi.io/api/TimeZone/coordinate?latitude={lat}&longitude={lon}"
        tz_resp = requests.get(tz_url)
        if tz_resp.status_code == 200:
            tz_data = tz_resp.json()
            return f"The timezone for {location.title()} is {tz_data['timeZone']}."
        else:
            return "Could not retrieve timezone information."
    except Exception as e:
        return f"Error during timezone lookup: {e}"

tools = [get_weather, get_time, get_timezone]

system_message = (
    "You are a helpful assistant. "
    "For every location the user provides, ALWAYS call ALL available tools: get_weather, get_time, and get_timezone. "
    "Do not ask for clarification. "
    "If a tool fails, include its error message in your answer. "
    "Combine the results from all tools into a single, clear response."
)
agent_executor = create_react_agent(llm, tools, prompt=system_message)

user_input = input("Ask about the weather, time, or timezone in a location: ")
messages = [("human", user_input)]

result = agent_executor.invoke(
    {"messages": messages},
    #config={"callbacks": [ConsoleCallbackHandler()]}
)
ai_response = result["messages"][-1].content
print(ai_response)