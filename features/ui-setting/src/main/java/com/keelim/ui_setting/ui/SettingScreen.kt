package com.keelim.ui_setting.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SettingScreen(
    onScreenAction: (Section) -> Unit,
) {
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)) {
                Text(
                    text = "설정",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            CellItem("Speaker") {
                onScreenAction(Section.Developer)
            }
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun CellItem(
    text: String,
    onClicked: () -> Unit
) {
    Text(
        text = text,
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 62.dp)
            .clickable { onClicked() }
            .padding(horizontal = 24.dp)
            .wrapContentHeight()
    )
}
