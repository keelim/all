package com.keelim.compose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.lang.reflect.Modifier

@Preview
@Composable
fun OneTwoThreeRow() {
    Row {
        Text(
           text = "One",
           fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeRow2() {
    Row(horizontalArrangement = Arrangement.Center) {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeColumn() {
    Column {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeColumn2() {
    Column(verticalArrangement = Arrangement.Center)  {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}