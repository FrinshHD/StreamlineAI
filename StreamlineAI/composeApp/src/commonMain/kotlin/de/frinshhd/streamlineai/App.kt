package de.frinshhd.streamlineai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import de.frinshhd.streamlineai.auth.ui.GoogleButtonUiContainer
import de.frinshhd.streamlineai.models.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import streamlineai.composeapp.generated.resources.Res
import streamlineai.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(content: @Composable () -> Unit = {}) {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }

                var signedInUser: User? by remember { mutableStateOf<User?>(null) }

                GoogleButtonUiContainer(onGoogleSignInResult = { user ->
                    signedInUser = user
                }) {
                    Button(
                        onClick = { this.onClick() }
                    ) {
                        Text("Sign-In with Google")
                    }
                }

                if (signedInUser != null) {
                    Text("Signed in as: ${signedInUser?.displayName ?: "Unknown"}")
                    AsyncImage(
                        model = signedInUser?.pictureUrl,
                        contentDescription = "User Profile Picture",
                        modifier = Modifier.fillMaxWidth()
                    )

                    content()
                }
            }
        }
    }
}

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDarkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}