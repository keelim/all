package com.keelim.comssa.ui.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(modifier: Modifier = Modifier, sectionClick: ((String) -> Unit)? = null) {

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(
            text = "Applications\nmade by keelim",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Card(
                onClick = { sectionClick?.invoke("com.keelim.cnubus") },
                modifier = Modifier.height(56.dp).fillMaxWidth()
            ) {
                Text(
                    text = "CnuBus",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp, 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Card(
                onClick = { sectionClick?.invoke("com.keelim.cnubus") },
                modifier = Modifier.height(56.dp).fillMaxWidth()
            ) {
                Text(
                    text = "MyGrade",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp, 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Card(
                onClick = { sectionClick?.invoke("com.keelim.cnubus") },
                modifier = Modifier.height(56.dp).fillMaxWidth()
            ) {
                Text(
                    text = "Comssa",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp, 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen()
}
