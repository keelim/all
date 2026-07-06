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
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8

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
                .padding(space24)
                .widthIn(max = 600.dp)
                .fillMaxWidth(),
        ) {
            KuiText(
                text = "Welcome! to our app",
                style = KuiTheme.typography.headlineLarge,
                color = Color.White,
            )
            Spacer(
                modifier = Modifier.height(space8),
            )
            KuiText(
                text = "We hope you find what you're looking for here.",
                style = KuiTheme.typography.headlineSmall,
                color = Color.White,
            )
            Spacer(Modifier.height(48.dp))
            KuiButton(
                onClick = onNavigateMain,
                modifier = Modifier.fillMaxWidth(),
            ) {
                KuiText(
                    text = "Let`s Move",
                    modifier = Modifier.padding(vertical = space8),
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
