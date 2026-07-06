package com.keelim.nandadiagnosis.ui.screen.medication

import android.Manifest
import android.os.Build
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
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.data.model.Medication
import com.keelim.data.model.MedicationFrequency
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val notificationPermissions: List<String> = buildList {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun MedicationRoute(
    viewModel: MedicationViewModel = hiltViewModel(),
) {
    val medications by viewModel.medications.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()
    val editingMedication by viewModel.editingMedication.collectAsStateWithLifecycle()

    // Request notification permission for Android 13+
    SimpleAcquirePermissions(
        permissions = notificationPermissions,
    ) { }

    MedicationScreen(
        medications = medications.toImmutableList(),
        showAddDialog = showAddDialog,
        editingMedication = editingMedication,
        onToggleMedication = viewModel::toggleMedication,
        onAddMedication = viewModel::addMedication,
        onUpdateMedication = viewModel::updateMedication,
        onRemoveMedication = viewModel::removeMedication,
        onEditMedication = viewModel::showEditDialog,
        onShowAddDialog = viewModel::showAddDialog,
        onHideAddDialog = viewModel::hideAddDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    medications: ImmutableList<Medication>,
    showAddDialog: Boolean,
    editingMedication: Medication?,
    onToggleMedication: (Medication) -> Unit,
    onAddMedication: (String, String, Int, Int) -> Unit,
    onUpdateMedication: (String, String, Int, Int) -> Unit,
    onRemoveMedication: (Medication) -> Unit,
    onEditMedication: (Medication) -> Unit,
    onShowAddDialog: () -> Unit,
    onHideAddDialog: () -> Unit
) {
    var medicationName by remember { mutableStateOf("") }
    var medicationDosage by remember { mutableStateOf("") }
    val timePickerState = rememberTimePickerState(initialHour = 9, initialMinute = 0)

    // Pre-fill form when editing
    val isEditing = editingMedication != null
    if (showAddDialog && isEditing && editingMedication != null) {
        medicationName = editingMedication.name
        medicationDosage = editingMedication.dosage
    }

    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KuiIcon(
                            imageVector = MedicationIcon,
                            contentDescription = null,
                            tint = KuiTheme.colorScheme.primary
                        )
                        KuiText(
                            text = "복약 알림",
                            style = KuiTheme.typography.titleLarge,
                            color = KuiTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            KuiFloatingActionButton(
                onClick = onShowAddDialog,
                containerColor = KuiTheme.colorScheme.primary
            ) {
                KuiIcon(
                    Icons.Default.Add,
                    contentDescription = "복약 추가",
                    tint = KuiTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        if (medications.isEmpty()) {
            EmptyMedicationContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    KuiText(
                        text = "등록된 복약 알림",
                        style = KuiTheme.typography.titleMedium,
                        color = KuiTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(medications, key = { it.id }) { medication ->
                    MedicationItem(
                        medication = medication,
                        onToggle = { onToggleMedication(medication) },
                        onDelete = { onRemoveMedication(medication) },
                        onEdit = { onEditMedication(medication) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        KuiAlertDialog(
            onDismissRequest = {
                medicationName = ""
                medicationDosage = ""
                onHideAddDialog()
            },
            title = {
                KuiText(
                    text = "복약 알림 추가",
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    KuiOutlinedTextField(
                        value = medicationName,
                        onValueChange = { medicationName = it },
                        label = {
                            KuiText(
                                text = "약물명",
                                style = KuiTheme.typography.labelMedium,
                                color = KuiTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    KuiOutlinedTextField(
                        value = medicationDosage,
                        onValueChange = { medicationDosage = it },
                        label = {
                            KuiText(
                                text = "복용량 (예: 1정, 5ml)",
                                style = KuiTheme.typography.labelMedium,
                                color = KuiTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    KuiText(
                        text = "복용 시간",
                        style = KuiTheme.typography.labelMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    KuiTimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                KuiTextButton(
                    onClick = {
                        if (medicationName.isNotBlank()) {
                            if (isEditing) {
                                onUpdateMedication(
                                    medicationName,
                                    medicationDosage.ifBlank { "1회분" },
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            } else {
                                onAddMedication(
                                    medicationName,
                                    medicationDosage.ifBlank { "1회분" },
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            }
                            medicationName = ""
                            medicationDosage = ""
                        }
                    }
                ) {
                    KuiText(
                        text = if (isEditing) "수정" else "추가",
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                KuiTextButton(
                    onClick = {
                        medicationName = ""
                        medicationDosage = ""
                        onHideAddDialog()
                    }
                ) {
                    KuiText(
                        text = "취소",
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Composable
private fun EmptyMedicationContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KuiIcon(
                imageVector = MedicationIcon,
                contentDescription = null,
                tint = KuiTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            KuiText(
                text = "등록된 복약 알림이 없습니다",
                style = KuiTheme.typography.bodyLarge,
                color = KuiTheme.colorScheme.onSurfaceVariant
            )
            KuiText(
                text = "+ 버튼을 눌러 복약 알림을 추가하세요",
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationItem(
    medication: Medication,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState()
    var hasHandledDelete by remember { mutableStateOf(false) }

    LaunchedEffect(dismissState.currentValue) {
        if (!hasHandledDelete && dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            hasHandledDelete = true
            onDelete()
        }
    }

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
                    contentDescription = "삭제",
                    tint = Color.White
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        MedicationCard(
            medication = medication,
            onToggle = onToggle,
            onDelete = onDelete,
            onEdit = onEdit
        )
    }
}

@Composable
private fun MedicationCard(
    medication: Medication,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    KuiCard(padded = false,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (medication.isEnabled) {
                KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                KuiTheme.colorScheme.surfaceVariant
            }
        ),
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KuiText(
                        text = "💊",
                        style = KuiTheme.typography.titleMedium
                    )
                    KuiText(
                        text = medication.name,
                        style = KuiTheme.typography.bodyLarge,
                        color = KuiTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
                KuiText(
                    text = medication.dosage,
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant
                )
                KuiText(
                    text = formatUiTime(hour = medication.hour, minute = medication.minute),
                    style = KuiTheme.typography.headlineMedium,
                    color = if (medication.isEnabled) {
                        KuiTheme.colorScheme.primary
                    } else {
                        KuiTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Bold
                )
                KuiText(
                    text = when (medication.frequency) {
                        MedicationFrequency.DAILY -> "매일"
                        MedicationFrequency.EVERY_OTHER_DAY -> "격일"
                        MedicationFrequency.SPECIFIC_DAYS -> "특정 요일"
                    },
                    style = KuiTheme.typography.labelSmall,
                    color = KuiTheme.colorScheme.outline
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                KuiIconButton(onClick = onDelete) {
                    KuiIcon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = KuiTheme.colorScheme.error
                    )
                }
                KuiSwitch(
                    checked = medication.isEnabled,
                    onCheckedChange = { onToggle() }
                )
            }
        }
    }
}
