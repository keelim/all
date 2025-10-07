package com.keelim.composeutil.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4

@Preview
@Composable
fun GreetingPreview() {
    Column(
        modifier = Modifier.padding(vertical = space4),
    ) {
        Greetings()
    }
}

@Preview
@Composable
fun OnBoardingPreview() {
    OnBoarding()
}

