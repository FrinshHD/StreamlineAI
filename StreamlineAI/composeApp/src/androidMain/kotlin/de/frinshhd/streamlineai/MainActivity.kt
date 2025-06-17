package de.frinshhd.streamlineai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import de.frinshhd.streamlineai.auth.AuthManager
import de.frinshhd.streamlineai.auth.GoogleAuthenticator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Handle OAuth callback
        intent?.data?.let { uri ->
            if (uri.toString().startsWith("myapp://callback")) {
                // You may want to exchange this with your Ktor server for session info
                val token = uri.getQueryParameter("token")
                Log.d("OAuth", "Received token: $token")
            }
        }

        setContent {
            App {
                GoogleAuthentic()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    Button(onClick = { AuthManager.loginWithGoogle(context) }) {
        Text("Login with Google")
    }
}

@Composable
fun GoogleAuthentic() {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val googleAuthenticator =
        remember {
            GoogleAuthenticator(
                context = context, credentialManager =
                    CredentialManager.create(context)
            )
        }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name)
        Button(onClick = {
            scope.launch {
                name = googleAuthenticator.login()

            }
        }) {
            Text(text = "Google log in")
        }

    }
}