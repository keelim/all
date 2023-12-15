package com.keelim.setting.screen.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeRoute(
    onNavigateMain: () -> Unit,
) {
    WelcomeScreen(
        onNavigateMain = onNavigateMain,
    )
}

@Composable
private fun WelcomeScreen(
    onNavigateMain: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black))))
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .widthIn(max = 600.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Welcome! to our app",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
            Spacer(
                modifier = Modifier.height(8.dp),
            )
            Text(
                text = "We hope you find what you're looking for here.",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
            )
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = onNavigateMain,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Let`s Move",
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(
        onNavigateMain = {},
    )
}
