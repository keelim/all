package com.keelim.comssa.ui.screen.main.flash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace

@Composable
fun FrontCardSection() = trace("FrontCardSection") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Front",
            color = Color.White,
            fontSize = 32.sp,
        )
    }
}

@Composable
fun BackCardSection() = trace("BackCardSection") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Back",
            color = Color.White,
            fontSize = 32.sp,
        )
    }
}

@Preview
@Composable
fun PreviewFrontCardSection() {
    FrontCardSection()
}

@Preview
@Composable
fun PreviewBackCardSection() {
    BackCardSection()
}
