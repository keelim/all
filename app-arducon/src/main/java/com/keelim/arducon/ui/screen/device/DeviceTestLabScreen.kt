@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.keelim.arducon.ui.screen.device

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiCircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiOutlinedButton
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.resource.Res
import com.keelim.core.resource.arducon_device_lab_action_reset
import com.keelim.core.resource.arducon_device_lab_action_run
import com.keelim.core.resource.arducon_device_lab_detail_capability_available
import com.keelim.core.resource.arducon_device_lab_detail_failed
import com.keelim.core.resource.arducon_device_lab_detail_failed_named
import com.keelim.core.resource.arducon_device_lab_detail_intent_launched
import com.keelim.core.resource.arducon_device_lab_detail_intent_unavailable
import com.keelim.core.resource.arducon_device_lab_detail_location_ready
import com.keelim.core.resource.arducon_device_lab_detail_microphone_captured
import com.keelim.core.resource.arducon_device_lab_detail_missing_feature
import com.keelim.core.resource.arducon_device_lab_detail_network_available
import com.keelim.core.resource.arducon_device_lab_detail_network_unavailable
import com.keelim.core.resource.arducon_device_lab_detail_no_location_provider
import com.keelim.core.resource.arducon_device_lab_detail_permission_denied
import com.keelim.core.resource.arducon_device_lab_detail_ready
import com.keelim.core.resource.arducon_device_lab_detail_running
import com.keelim.core.resource.arducon_device_lab_detail_scanner_canceled
import com.keelim.core.resource.arducon_device_lab_detail_scanner_launched
import com.keelim.core.resource.arducon_device_lab_detail_scanner_result
import com.keelim.core.resource.arducon_device_lab_detail_sensors_found
import com.keelim.core.resource.arducon_device_lab_detail_vibration_sent
import com.keelim.core.resource.arducon_device_lab_hardware_camera_desc
import com.keelim.core.resource.arducon_device_lab_hardware_camera_title
import com.keelim.core.resource.arducon_device_lab_hardware_location_desc
import com.keelim.core.resource.arducon_device_lab_hardware_location_title
import com.keelim.core.resource.arducon_device_lab_hardware_microphone_desc
import com.keelim.core.resource.arducon_device_lab_hardware_microphone_title
import com.keelim.core.resource.arducon_device_lab_hardware_network_desc
import com.keelim.core.resource.arducon_device_lab_hardware_network_title
import com.keelim.core.resource.arducon_device_lab_hardware_qr_desc
import com.keelim.core.resource.arducon_device_lab_hardware_qr_title
import com.keelim.core.resource.arducon_device_lab_hardware_sensors_desc
import com.keelim.core.resource.arducon_device_lab_hardware_sensors_title
import com.keelim.core.resource.arducon_device_lab_hardware_vibration_desc
import com.keelim.core.resource.arducon_device_lab_hardware_vibration_title
import com.keelim.core.resource.arducon_device_lab_intent_app_settings_desc
import com.keelim.core.resource.arducon_device_lab_intent_app_settings_title
import com.keelim.core.resource.arducon_device_lab_intent_browser_desc
import com.keelim.core.resource.arducon_device_lab_intent_browser_title
import com.keelim.core.resource.arducon_device_lab_intent_dialer_desc
import com.keelim.core.resource.arducon_device_lab_intent_dialer_title
import com.keelim.core.resource.arducon_device_lab_intent_email_desc
import com.keelim.core.resource.arducon_device_lab_intent_email_title
import com.keelim.core.resource.arducon_device_lab_intent_map_desc
import com.keelim.core.resource.arducon_device_lab_intent_map_title
import com.keelim.core.resource.arducon_device_lab_intent_notification_desc
import com.keelim.core.resource.arducon_device_lab_intent_notification_title
import com.keelim.core.resource.arducon_device_lab_intent_share_desc
import com.keelim.core.resource.arducon_device_lab_intent_share_title
import com.keelim.core.resource.arducon_device_lab_section_hardware
import com.keelim.core.resource.arducon_device_lab_section_intents
import com.keelim.core.resource.arducon_device_lab_status_fail
import com.keelim.core.resource.arducon_device_lab_status_pass
import com.keelim.core.resource.arducon_device_lab_status_ready
import com.keelim.core.resource.arducon_device_lab_status_running
import com.keelim.core.resource.arducon_device_lab_status_skipped
import com.keelim.core.resource.arducon_device_lab_subtitle
import com.keelim.core.resource.arducon_device_lab_summary
import com.keelim.core.resource.arducon_device_lab_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DeviceTestLabRoute(
    onNavigateBack: () -> Unit,
    viewModel: DeviceTestLabViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var pendingCameraTest by remember { mutableStateOf<DeviceTestId?>(null) }
    var pendingMicrophoneTest by remember { mutableStateOf<DeviceTestId?>(null) }
    var pendingLocationTest by remember { mutableStateOf<DeviceTestId?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        pendingCameraTest?.let { id ->
            if (granted) {
                runHardwareTest(
                    context = context,
                    id = id,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                )
            } else {
                viewModel.applyOutcome(
                    id = id,
                    outcome = DeviceTestOutcome.fail(DeviceTestMessage.PermissionDenied),
                )
            }
        }
        pendingCameraTest = null
    }

    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        pendingMicrophoneTest?.let { id ->
            if (granted) {
                runHardwareTest(
                    context = context,
                    id = id,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                )
            } else {
                viewModel.applyOutcome(
                    id = id,
                    outcome = DeviceTestOutcome.fail(DeviceTestMessage.PermissionDenied),
                )
            }
        }
        pendingMicrophoneTest = null
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        pendingLocationTest?.let { id ->
            if (grants.values.any { it }) {
                runHardwareTest(
                    context = context,
                    id = id,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                )
            } else {
                viewModel.applyOutcome(
                    id = id,
                    outcome = DeviceTestOutcome.fail(DeviceTestMessage.PermissionDenied),
                )
            }
        }
        pendingLocationTest = null
    }

    DeviceTestLabScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRunTest = { id ->
            when (id) {
                DeviceTestId.Camera,
                DeviceTestId.QrScanner,
                -> if (context.hasPermission(Manifest.permission.CAMERA)) {
                    runHardwareTest(
                        context = context,
                        id = id,
                        viewModel = viewModel,
                        coroutineScope = coroutineScope,
                    )
                } else {
                    viewModel.markRunning(id)
                    pendingCameraTest = id
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }

                DeviceTestId.Microphone -> if (context.hasPermission(Manifest.permission.RECORD_AUDIO)) {
                    runHardwareTest(
                        context = context,
                        id = id,
                        viewModel = viewModel,
                        coroutineScope = coroutineScope,
                    )
                } else {
                    viewModel.markRunning(id)
                    pendingMicrophoneTest = id
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }

                DeviceTestId.Location -> {
                    val hasLocationPermission = context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    if (hasLocationPermission) {
                        runHardwareTest(
                            context = context,
                            id = id,
                            viewModel = viewModel,
                            coroutineScope = coroutineScope,
                        )
                    } else {
                        viewModel.markRunning(id)
                        pendingLocationTest = id
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ),
                        )
                    }
                }

                in hardwareTestIds -> runHardwareTest(
                    context = context,
                    id = id,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                )

                in systemIntentTestIds -> {
                    viewModel.markRunning(id)
                    viewModel.applyOutcome(
                        id = id,
                        outcome = DeviceIntentTests.run(context, id),
                    )
                }

                else -> Unit
            }
        },
        onResetTest = viewModel::reset,
    )
}

@Composable
private fun DeviceTestLabScreen(
    uiState: DeviceTestLabUiState,
    onNavigateBack: () -> Unit,
    onRunTest: (DeviceTestId) -> Unit,
    onResetTest: (DeviceTestId) -> Unit,
) {
    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    KuiText(
                        text = stringResource(Res.string.arducon_device_lab_title),
                        style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = KuiTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    KuiIconButton(onClick = onNavigateBack) {
                        KuiIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = KuiTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(space16),
            verticalArrangement = Arrangement.spacedBy(space12),
        ) {
            item {
                DeviceLabSummary(uiState = uiState)
            }

            item {
                DeviceLabSectionTitle(title = Res.string.arducon_device_lab_section_hardware)
            }

            items(
                items = hardwareRows,
                key = { it.id },
            ) { spec ->
                DeviceTestCard(
                    spec = spec,
                    result = uiState.results.getValue(spec.id),
                    onRun = { onRunTest(spec.id) },
                    onReset = { onResetTest(spec.id) },
                    modifier = Modifier.animateItem(),
                )
            }

            item {
                DeviceLabSectionTitle(title = Res.string.arducon_device_lab_section_intents)
            }

            items(
                items = systemIntentRows,
                key = { it.id },
            ) { spec ->
                DeviceTestCard(
                    spec = spec,
                    result = uiState.results.getValue(spec.id),
                    onRun = { onRunTest(spec.id) },
                    onReset = { onResetTest(spec.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun DeviceLabSummary(
    uiState: DeviceTestLabUiState,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(space16),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                KuiIcon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = KuiTheme.colorScheme.primary,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(space4),
                ) {
                    KuiText(
                        text = stringResource(Res.string.arducon_device_lab_title),
                        style = KuiTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = KuiTheme.colorScheme.onSurface,
                    )
                    KuiText(
                        text = stringResource(Res.string.arducon_device_lab_subtitle),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            KuiText(
                text = stringResource(
                    Res.string.arducon_device_lab_summary,
                    uiState.passCount,
                    uiState.failCount,
                    uiState.runningCount,
                    uiState.totalCount,
                ),
                style = KuiTheme.typography.bodySmall,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DeviceLabSectionTitle(title: StringResource) {
    KuiText(
        text = stringResource(title),
        style = KuiTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = KuiTheme.colorScheme.primary,
        modifier = Modifier.padding(top = space4),
    )
}

@Composable
private fun DeviceTestCard(
    spec: DeviceTestRowSpec,
    result: DeviceTestResult,
    onRun: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(space12),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space12),
                verticalAlignment = Alignment.Top,
            ) {
                KuiIcon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = KuiTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(space4),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        KuiText(
                            text = stringResource(spec.title),
                            style = KuiTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = KuiTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )
                        DeviceStatusChip(status = result.status)
                    }
                    KuiText(
                        text = stringResource(spec.description),
                        style = KuiTheme.typography.bodySmall,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                    KuiText(
                        text = resultText(result),
                        style = KuiTheme.typography.bodySmall,
                        color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                KuiOutlinedButton(
                    onClick = onReset,
                    enabled = result.status != DeviceTestStatus.Running,
                ) {
                    KuiIcon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    KuiText(
                        text = stringResource(Res.string.arducon_device_lab_action_reset),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.primary,
                    )
                }
                KuiButton(
                    onClick = onRun,
                    enabled = result.status != DeviceTestStatus.Running,
                    modifier = Modifier.padding(start = space8),
                ) {
                    AnimatedVisibility(
                        visible = result.status == DeviceTestStatus.Running,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        KuiCircularProgressIndicator(
                            modifier = Modifier
                                .size(18.dp)
                                .padding(end = space4),
                            strokeWidth = 2.dp,
                            color = KuiTheme.colorScheme.onPrimary,
                        )
                    }
                    KuiIcon(
                        imageVector = if (result.status == DeviceTestStatus.Pass) {
                            Icons.Default.Check
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    KuiText(
                        text = stringResource(Res.string.arducon_device_lab_action_run),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceStatusChip(status: DeviceTestStatus) {
    KuiSurface(
        color = statusContainerColor(status),
        contentColor = statusContentColor(status),
        shape = KuiTheme.shapes.small,
    ) {
        KuiText(
            text = statusLabel(status),
            style = KuiTheme.typography.labelSmall,
            color = statusContentColor(status),
            modifier = Modifier.padding(horizontal = space8, vertical = space4),
        )
    }
}

@Composable
private fun statusLabel(status: DeviceTestStatus): String {
    return when (status) {
        DeviceTestStatus.Ready -> stringResource(Res.string.arducon_device_lab_status_ready)
        DeviceTestStatus.Running -> stringResource(Res.string.arducon_device_lab_status_running)
        DeviceTestStatus.Pass -> stringResource(Res.string.arducon_device_lab_status_pass)
        DeviceTestStatus.Fail -> stringResource(Res.string.arducon_device_lab_status_fail)
        DeviceTestStatus.Skipped -> stringResource(Res.string.arducon_device_lab_status_skipped)
    }
}

@Composable
private fun statusContainerColor(status: DeviceTestStatus): Color {
    return when (status) {
        DeviceTestStatus.Ready -> KuiTheme.colorScheme.surface
        DeviceTestStatus.Running -> KuiTheme.colorScheme.primaryContainer
        DeviceTestStatus.Pass -> KuiTheme.colorScheme.tertiaryContainer
        DeviceTestStatus.Fail -> KuiTheme.colorScheme.errorContainer
        DeviceTestStatus.Skipped -> KuiTheme.colorScheme.secondaryContainer
    }
}

@Composable
private fun statusContentColor(status: DeviceTestStatus): Color {
    return when (status) {
        DeviceTestStatus.Ready -> KuiTheme.colorScheme.onSurface
        DeviceTestStatus.Running -> KuiTheme.colorScheme.onPrimaryContainer
        DeviceTestStatus.Pass -> KuiTheme.colorScheme.onTertiaryContainer
        DeviceTestStatus.Fail -> KuiTheme.colorScheme.onErrorContainer
        DeviceTestStatus.Skipped -> KuiTheme.colorScheme.onSecondaryContainer
    }
}

@Composable
private fun resultText(result: DeviceTestResult): String {
    return when (result.message) {
        DeviceTestMessage.Ready -> stringResource(Res.string.arducon_device_lab_detail_ready)
        DeviceTestMessage.Running -> stringResource(Res.string.arducon_device_lab_detail_running)
        DeviceTestMessage.CapabilityAvailable -> stringResource(
            Res.string.arducon_device_lab_detail_capability_available,
        )

        DeviceTestMessage.PermissionDenied -> stringResource(
            Res.string.arducon_device_lab_detail_permission_denied,
        )

        DeviceTestMessage.MissingFeature -> stringResource(
            Res.string.arducon_device_lab_detail_missing_feature,
        )

        DeviceTestMessage.ScannerLaunched -> stringResource(
            Res.string.arducon_device_lab_detail_scanner_launched,
        )

        DeviceTestMessage.ScannerCanceled -> stringResource(
            Res.string.arducon_device_lab_detail_scanner_canceled,
        )

        DeviceTestMessage.ScannerResult -> stringResource(
            Res.string.arducon_device_lab_detail_scanner_result,
            result.detail,
        )

        DeviceTestMessage.MicrophoneCaptured -> stringResource(
            Res.string.arducon_device_lab_detail_microphone_captured,
            result.detail,
        )

        DeviceTestMessage.VibrationSent -> stringResource(
            Res.string.arducon_device_lab_detail_vibration_sent,
        )

        DeviceTestMessage.SensorsFound -> stringResource(
            Res.string.arducon_device_lab_detail_sensors_found,
            result.detail,
        )

        DeviceTestMessage.NetworkAvailable -> stringResource(
            Res.string.arducon_device_lab_detail_network_available,
            result.detail,
        )

        DeviceTestMessage.NetworkUnavailable -> stringResource(
            Res.string.arducon_device_lab_detail_network_unavailable,
        )

        DeviceTestMessage.LocationReady -> stringResource(
            Res.string.arducon_device_lab_detail_location_ready,
            result.detail,
        )

        DeviceTestMessage.NoLocationProvider -> stringResource(
            Res.string.arducon_device_lab_detail_no_location_provider,
        )

        DeviceTestMessage.IntentLaunched -> stringResource(
            Res.string.arducon_device_lab_detail_intent_launched,
        )

        DeviceTestMessage.IntentUnavailable -> stringResource(
            Res.string.arducon_device_lab_detail_intent_unavailable,
        )

        DeviceTestMessage.Failed -> if (result.detail.isBlank()) {
            stringResource(Res.string.arducon_device_lab_detail_failed)
        } else {
            stringResource(Res.string.arducon_device_lab_detail_failed_named, result.detail)
        }
    }
}

private fun runHardwareTest(
    context: Context,
    id: DeviceTestId,
    viewModel: DeviceTestLabViewModel,
    coroutineScope: CoroutineScope,
) {
    viewModel.markRunning(id)
    when (id) {
        DeviceTestId.Camera -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.checkCamera(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        DeviceTestId.QrScanner -> runQrScanner(context, viewModel)

        DeviceTestId.Microphone -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.probeMicrophone(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        DeviceTestId.Vibration -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.vibrate(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        DeviceTestId.Sensors -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.summarizeSensors(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        DeviceTestId.Network -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.checkNetwork(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        DeviceTestId.Location -> coroutineScope.launch {
            val outcome = withContext(Dispatchers.IO) {
                DeviceHardwareTests.checkLocationReadiness(context)
            }
            viewModel.applyOutcome(id, outcome)
        }

        else -> viewModel.applyOutcome(id, DeviceTestOutcome.fail(DeviceTestMessage.Failed))
    }
}

private fun runQrScanner(
    context: Context,
    viewModel: DeviceTestLabViewModel,
) {
    val cameraOutcome = DeviceHardwareTests.checkCamera(context)
    if (cameraOutcome.status != DeviceTestStatus.Pass) {
        viewModel.applyOutcome(DeviceTestId.QrScanner, cameraOutcome)
        return
    }

    GmsBarcodeScanning.getClient(context)
        .startScan()
        .addOnSuccessListener { barcode ->
            val rawValue = barcode.rawValue.orEmpty()
            viewModel.applyOutcome(
                id = DeviceTestId.QrScanner,
                outcome = if (rawValue.isBlank()) {
                    DeviceTestOutcome.pass(DeviceTestMessage.ScannerLaunched)
                } else {
                    DeviceTestOutcome.pass(DeviceTestMessage.ScannerResult, rawValue.take(MAX_QR_DETAIL_LENGTH))
                },
            )
        }
        .addOnCanceledListener {
            viewModel.applyOutcome(
                id = DeviceTestId.QrScanner,
                outcome = DeviceTestOutcome.skipped(DeviceTestMessage.ScannerCanceled),
            )
        }
        .addOnFailureListener { throwable ->
            viewModel.applyOutcome(
                id = DeviceTestId.QrScanner,
                outcome = DeviceTestOutcome.fail(DeviceTestMessage.Failed, throwable.javaClass.simpleName),
            )
        }
}

private data class DeviceTestRowSpec(
    val id: DeviceTestId,
    val title: StringResource,
    val description: StringResource,
)

private val hardwareRows = listOf(
    DeviceTestRowSpec(
        id = DeviceTestId.Camera,
        title = Res.string.arducon_device_lab_hardware_camera_title,
        description = Res.string.arducon_device_lab_hardware_camera_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.QrScanner,
        title = Res.string.arducon_device_lab_hardware_qr_title,
        description = Res.string.arducon_device_lab_hardware_qr_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.Microphone,
        title = Res.string.arducon_device_lab_hardware_microphone_title,
        description = Res.string.arducon_device_lab_hardware_microphone_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.Vibration,
        title = Res.string.arducon_device_lab_hardware_vibration_title,
        description = Res.string.arducon_device_lab_hardware_vibration_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.Sensors,
        title = Res.string.arducon_device_lab_hardware_sensors_title,
        description = Res.string.arducon_device_lab_hardware_sensors_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.Network,
        title = Res.string.arducon_device_lab_hardware_network_title,
        description = Res.string.arducon_device_lab_hardware_network_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.Location,
        title = Res.string.arducon_device_lab_hardware_location_title,
        description = Res.string.arducon_device_lab_hardware_location_desc,
    ),
)

private val systemIntentRows = listOf(
    DeviceTestRowSpec(
        id = DeviceTestId.BrowserIntent,
        title = Res.string.arducon_device_lab_intent_browser_title,
        description = Res.string.arducon_device_lab_intent_browser_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.ShareIntent,
        title = Res.string.arducon_device_lab_intent_share_title,
        description = Res.string.arducon_device_lab_intent_share_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.AppSettingsIntent,
        title = Res.string.arducon_device_lab_intent_app_settings_title,
        description = Res.string.arducon_device_lab_intent_app_settings_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.NotificationSettingsIntent,
        title = Res.string.arducon_device_lab_intent_notification_title,
        description = Res.string.arducon_device_lab_intent_notification_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.DialerIntent,
        title = Res.string.arducon_device_lab_intent_dialer_title,
        description = Res.string.arducon_device_lab_intent_dialer_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.EmailIntent,
        title = Res.string.arducon_device_lab_intent_email_title,
        description = Res.string.arducon_device_lab_intent_email_desc,
    ),
    DeviceTestRowSpec(
        id = DeviceTestId.MapIntent,
        title = Res.string.arducon_device_lab_intent_map_title,
        description = Res.string.arducon_device_lab_intent_map_desc,
    ),
)

private const val MAX_QR_DETAIL_LENGTH = 80
