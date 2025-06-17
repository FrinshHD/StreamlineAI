from dotenv import load_dotenv
import os
from langchain_google_genai import GoogleGenerativeAI

load_dotenv()  # Loads variables from .env into environment

api_key = os.getenv("GEMINI_API_KEY")
if not api_key:
    raise ValueError("GEMINI_API_KEY not found in .env file")


model = GoogleGenerativeAI(
    model="gemini-2.0-flash",
    google_api_key=api_key,
)

response = model.invoke("What is the capital of France?")

print(response)