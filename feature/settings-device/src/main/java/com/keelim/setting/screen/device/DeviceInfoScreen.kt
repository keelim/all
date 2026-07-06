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
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space8
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

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

    val deviceInfos = if (uiState.deviceName.isEmpty()) {
        emptyList()
    } else {
        listOf(
            DeviceInfoItem(stringResource(Res.string.settings_device_label_model), uiState.deviceModel),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_manufacturer), uiState.deviceBrand),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_device), uiState.deviceName),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_brand), uiState.deviceBrand),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_board), uiState.board),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_hardware), uiState.hardware),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_product), uiState.product),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_android_version), uiState.versionName),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_app_version), uiState.versionName),
            DeviceInfoItem(stringResource(Res.string.settings_device_label_sdk_level), uiState.sdkLevel.toUiNumber()),
            DeviceInfoItem(stringResource(Res.string.settings_device_screen_density), stringResource(Res.string.settings_device_screen_density_value, uiState.screenDensity.toUiNumber())),
            DeviceInfoItem(stringResource(Res.string.settings_device_screen_width), stringResource(Res.string.settings_device_screen_width_value, uiState.screenWidthDp.toUiNumber())),
            DeviceInfoItem(stringResource(Res.string.settings_device_screen_height), stringResource(Res.string.settings_device_screen_height_value, uiState.screenHeightDp.toUiNumber())),
            DeviceInfoItem(stringResource(Res.string.settings_device_supported_abis), uiState.supportedAbis.joinToString(", ")),
        )
    }

    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    KuiText(
                        text = stringResource(Res.string.settings_category_device_info),
                        style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = KuiTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    KuiIconButton(onClick = onNavigateBack) {
                        KuiIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.settings_back_description))
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
    KuiCard(padded = false,
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
            KuiText(
                text = item.label,
                style = KuiTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            KuiText(
                text = item.value,
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
        }
    }
}
