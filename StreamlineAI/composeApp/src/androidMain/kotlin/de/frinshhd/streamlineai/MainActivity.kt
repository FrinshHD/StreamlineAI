package de.frinshhd.streamlineai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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
            App {}
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}