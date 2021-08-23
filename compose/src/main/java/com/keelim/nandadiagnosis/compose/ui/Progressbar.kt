package com.keelim.nandadiagnosis.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun PreviewProgress(){
    CircularIndeterminateProgressBar(
        isDisplayed = true
    )
}


@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean,
){
    if(isDisplayed){
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }
}