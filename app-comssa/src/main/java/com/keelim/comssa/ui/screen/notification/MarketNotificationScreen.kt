package com.keelim.comssa.ui.screen.notification

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import com.keelim.core.designsystem.component.KuiSwitch
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import com.keelim.core.designsystem.component.KuiTimePicker
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.common.extensions.formatUiTime
import com.keelim.core.resource.Res
import com.keelim.core.resource.market_notifications_add
import com.keelim.core.resource.market_notifications_add_custom_notification
import com.keelim.core.resource.market_notifications_add_custom_time
import com.keelim.core.resource.market_notifications_cancel
import com.keelim.core.resource.market_notifications_default
import com.keelim.core.resource.market_notifications_delete
import com.keelim.core.resource.market_notifications_name_label
import com.keelim.core.resource.market_notifications_open_alerts
import com.keelim.core.resource.market_notifications_title
import com.keelim.data.model.MarketSchedule
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketNotificationScreen(
    viewModel: MarketNotificationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val schedules by viewModel.schedules.collectAsStateWithLifecycle()
    val showTimePicker by viewModel.showTimePicker.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    val timePickerState = rememberTimePickerState(initialHour = 9, initialMinute = 0)

    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    KuiText(
                        text = stringResource(Res.string.market_notifications_title),
                        style = KuiTheme.typography.titleLarge,
                        color = KuiTheme.colorScheme.onSurface
                    )
                }
            )
        },
        floatingActionButton = {
            KuiFloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                KuiIcon(
                    Icons.Default.Add,
                    contentDescription = stringResource(Res.string.market_notifications_add_custom_time)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                KuiText(
                    text = stringResource(Res.string.market_notifications_open_alerts),
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(schedules, key = { it.id }) { schedule ->
                MarketScheduleItem(
                    schedule = schedule,
                    onToggle = { viewModel.toggleSchedule(schedule) },
                    onDelete = if (!schedule.isDefault) {
                        { viewModel.removeSchedule(schedule) }
                    } else null
                )
            }
        }
    }

    if (showAddDialog) {
        KuiAlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                KuiText(
                    text = stringResource(Res.string.market_notifications_add_custom_notification),
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    KuiOutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = {
                            KuiText(
                                text = stringResource(Res.string.market_notifications_name_label),
                                style = KuiTheme.typography.labelMedium,
                                color = KuiTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    KuiTimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                KuiTextButton(
                    onClick = {
                        if (customName.isNotBlank()) {
                            viewModel.addCustomSchedule(
                                name = customName,
                                hour = timePickerState.hour,
                                minute = timePickerState.minute
                            )
                            customName = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    KuiText(
                        text = stringResource(Res.string.market_notifications_add),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                KuiTextButton(onClick = { showAddDialog = false }) {
                    KuiText(
                        text = stringResource(Res.string.market_notifications_cancel),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarketScheduleItem(
    schedule: MarketSchedule,
    onToggle: () -> Unit,
    onDelete: (() -> Unit)?
) {
    val dismissState = rememberSwipeToDismissBoxState()
    var hasHandledDelete by remember { mutableStateOf(false) }

    LaunchedEffect(dismissState.currentValue, onDelete) {
        if (!hasHandledDelete &&
            dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
            onDelete != null
        ) {
            hasHandledDelete = true
            onDelete()
        }
    }

    if (onDelete != null) {
        KuiSwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                val color by animateColorAsState(
                    when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                        else -> Color.Transparent
                    },
                    label = "background"
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color, RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    KuiIcon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(Res.string.market_notifications_delete),
                        tint = Color.White
                    )
                }
            },
            enableDismissFromStartToEnd = false
        ) {
            ScheduleCard(schedule = schedule, onToggle = onToggle, onDelete = onDelete)
        }
    } else {
        ScheduleCard(schedule = schedule, onToggle = onToggle, onDelete = null)
    }
}

@Composable
private fun ScheduleCard(
    schedule: MarketSchedule,
    onToggle: () -> Unit,
    onDelete: (() -> Unit)?
) {
    KuiCard(padded = false,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (schedule.isEnabled) {
                KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                KuiTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                KuiText(
                    text = schedule.name,
                    style = KuiTheme.typography.bodyLarge,
                    color = KuiTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                KuiText(
                    text = formatUiTime(hour = schedule.hour, minute = schedule.minute),
                    style = KuiTheme.typography.headlineMedium,
                    color = if (schedule.isEnabled) {
                        KuiTheme.colorScheme.primary
                    } else {
                        KuiTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Bold
                )
                if (schedule.isDefault) {
                    KuiText(
                        text = stringResource(Res.string.market_notifications_default),
                        style = KuiTheme.typography.labelSmall,
                        color = KuiTheme.colorScheme.outline
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onDelete != null) {
                    KuiIconButton(onClick = onDelete) {
                        KuiIcon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.market_notifications_delete),
                            tint = KuiTheme.colorScheme.error
                        )
                    }
                }
                KuiSwitch(
                    checked = schedule.isEnabled,
                    onCheckedChange = { onToggle() }
                )
            }
        }
    }
}
