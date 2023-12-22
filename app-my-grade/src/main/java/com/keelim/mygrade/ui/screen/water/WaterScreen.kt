package com.keelim.mygrade.ui.screen.water

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar

@Composable
fun WaterRoute() {
    WaterScreen()
}

@Composable
fun WaterScreen(viewModel: WaterViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        NavigationBackArrowBar(title = "물 마시기")
        var waterCount by remember { mutableIntStateOf(0) }

        Button(onClick = { waterCount++ }) {
            Icon(imageVector = Icons.Sharp.Add, contentDescription = null)
        }
        if (waterCount > 8) {
            Spacer(
                modifier = Modifier.height(10.dp),
            )
            Text(
                text = "물을 많이 드셨군요",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWaterScreen() {
    WaterScreen()
}
