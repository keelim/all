package com.keelim.nandadiagnosis.compose.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
private fun PreviewProgress(){
    CircularIndeterminateProgressBar(
        isDisplayed = true
    )
}


@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean,
){
    if(isDisplayed){
        CircularProgressIndicator(
            modifier = Modifier.padding(8.dp),
            color = Color.Yellow,
            strokeWidth = 10.dp
        )
    }
}