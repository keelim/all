package com.keelim.cnubus.ui.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val screens = listOf("Root A", "Root B", "Root C", "Settings")

@Composable
internal fun CnubusDrawer(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(
                paddingValues = PaddingValues(start = 24.dp, top = 48.dp),
            )
    ) {
        for (screen in screens) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen,
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCnubusDrawer() {
    MaterialTheme { CnubusDrawer() }
}
