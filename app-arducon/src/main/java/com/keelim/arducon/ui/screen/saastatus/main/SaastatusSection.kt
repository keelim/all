package com.keelim.arducon.ui.screen.saastatus.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4

@Composable
fun SaastatusEmpty(
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "지원 서비스 없음",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(
            modifier = Modifier.height(space4)
        )
        Text(
            text = "서비스를 등록해보세요.",
            style = MaterialTheme.typography.bodyMedium
        )

        IconButton(
            onClick = onRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun PreviewSaastatusEmpty() {
    SaastatusEmpty(
        onRegister = {},
    )
}
