package com.keelim.setting.screen.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space8

data class DeviceInfoItem(
    val label: String,
    val value: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(
    onNavigateBack: () -> Unit,
    viewModel: DeviceInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val deviceInfos = remember(uiState) {
        if (uiState.deviceName.isEmpty()) {
            emptyList()
        } else {
            listOf(
                DeviceInfoItem("Model", uiState.deviceModel),
                DeviceInfoItem("Manufacturer", uiState.deviceBrand),
                DeviceInfoItem("Device", uiState.deviceName),
                DeviceInfoItem("Brand", uiState.deviceBrand),
                DeviceInfoItem("Board", uiState.board),
                DeviceInfoItem("Hardware", uiState.hardware),
                DeviceInfoItem("Product", uiState.product),
                DeviceInfoItem("Android Version", uiState.versionName),
                DeviceInfoItem("App Version", uiState.versionName),
                DeviceInfoItem("SDK Level", uiState.sdkLevel.toString()),
                DeviceInfoItem("Screen Density", "${uiState.screenDensity} dpi"),
                DeviceInfoItem("Screen Width", "${uiState.screenWidthDp} dp"),
                DeviceInfoItem("Screen Height", "${uiState.screenHeightDp} dp"),
                DeviceInfoItem("Supported ABIs", uiState.supportedAbis.joinToString(", ")),
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Device Info",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = space16),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            items(deviceInfos) { item ->
                DeviceInfoCard(item)
            }
        }
    }
}

@Composable
fun DeviceInfoCard(item: DeviceInfoItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = space2),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
