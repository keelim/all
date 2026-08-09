@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource as androidStringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.common.extensions.formatUiTime
import com.keelim.comssa.R
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    val placementSpec = KuiTheme.motionScheme.defaultSpatialSpec<IntOffset>()

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
                    } else null,
                    modifier = Modifier.animateItem(placementSpec = placementSpec),
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
internal fun MarketScheduleItem(
    schedule: MarketSchedule,
    onToggle: () -> Unit,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState()
    val currentOnDelete by rememberUpdatedState(onDelete)
    val visibilityState = remember(schedule.id) { MutableTransitionState(true) }
    var deleteRequested by remember(schedule.id) { mutableStateOf(false) }
    var deleteDispatched by remember(schedule.id) { mutableStateOf(false) }
    val motionScheme = KuiTheme.motionScheme

    val requestDelete: () -> Unit = {
        if (!deleteRequested && currentOnDelete != null) {
            deleteRequested = true
            visibilityState.targetState = false
        }
    }

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            requestDelete()
        }
    }

    LaunchedEffect(
        deleteRequested,
        visibilityState.currentState,
        visibilityState.isIdle,
    ) {
        if (deleteRequested &&
            !deleteDispatched &&
            !visibilityState.currentState &&
            visibilityState.isIdle
        ) {
            deleteDispatched = true
            currentOnDelete?.invoke()
        }
    }

    AnimatedVisibility(
        visibleState = visibilityState,
        modifier = modifier,
        exit = fadeOut(animationSpec = motionScheme.fastEffectsSpec()) +
            scaleOut(
                targetScale = 0.96f,
                transformOrigin = TransformOrigin(0.5f, 0f),
                animationSpec = motionScheme.fastSpatialSpec(),
            ),
    ) {
        if (onDelete != null) {
            KuiSwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart ->
                                KuiTheme.colorScheme.error.copy(alpha = 0.88f)
                            else -> Color.Transparent
                        },
                        animationSpec = motionScheme.fastEffectsSpec(),
                        label = "deleteBackground",
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color, RoundedCornerShape(12.dp))
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        KuiIcon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(
                                Res.string.market_notifications_delete,
                            ),
                            tint = KuiTheme.colorScheme.onError,
                        )
                    }
                },
                enableDismissFromStartToEnd = false,
            ) {
                ScheduleCard(
                    schedule = schedule,
                    onToggle = onToggle,
                    onDelete = requestDelete,
                )
            }
        } else {
            ScheduleCard(schedule = schedule, onToggle = onToggle, onDelete = null)
        }
    }
}

@Composable
private fun ScheduleCard(
    schedule: MarketSchedule,
    onToggle: () -> Unit,
    onDelete: (() -> Unit)?,
) {
    val statusLabel = androidStringResource(
        if (schedule.isEnabled) {
            R.string.market_notification_status_enabled
        } else {
            R.string.market_notification_status_disabled
        },
    )
    val statusColor = if (schedule.isEnabled) {
        KuiTheme.colors.success
    } else {
        KuiTheme.colorScheme.onSurfaceVariant
    }

    KuiCard(padded = false,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = statusLabel },
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KuiIcon(
                        imageVector = if (schedule.isEnabled) {
                            Icons.Default.Notifications
                        } else {
                            Icons.Default.Close
                        },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.height(KuiTheme.spacing.space4),
                    )
                    KuiText(
                        text = statusLabel,
                        style = KuiTheme.typography.labelMedium,
                        color = statusColor,
                    )
                }
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
