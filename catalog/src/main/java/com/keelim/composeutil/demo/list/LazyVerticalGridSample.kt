package com.keelim.composeutil.demo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SampleCard() {
    KuiCard(padded = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KuiText(
                text = "Jetpack Compose",
                fontSize = 30.sp,
            )
            KuiText(
                "Card Example",
                fontSize = 20.sp,
            )
        }
    }
}
