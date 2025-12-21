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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = MedicationIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "복약 알림",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onShowAddDialog,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "복약 추가",
                    tint = MaterialTheme.colorScheme.onPrimary
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
                    Text(
                        text = "등록된 복약 알림",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
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
        AlertDialog(
            onDismissRequest = {
                medicationName = ""
                medicationDosage = ""
                onHideAddDialog()
            },
            title = {
                Text(
                    text = "복약 알림 추가",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = medicationName,
                        onValueChange = { medicationName = it },
                        label = {
                            Text(
                                text = "약물명",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = medicationDosage,
                        onValueChange = { medicationDosage = it },
                        label = {
                            Text(
                                text = "복용량 (예: 1정, 5ml)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "복용 시간",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(
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
                    Text(
                        text = if (isEditing) "수정" else "추가",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        medicationName = ""
                        medicationDosage = ""
                        onHideAddDialog()
                    }
                ) {
                    Text(
                        text = "취소",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            Icon(
                imageVector = MedicationIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "등록된 복약 알림이 없습니다",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "+ 버튼을 눌러 복약 알림을 추가하세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
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
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
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
                Icon(
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (medication.isEnabled) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
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
                    Text(
                        text = "💊",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = medication.dosage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = String.format("%02d:%02d", medication.hour, medication.minute),
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (medication.isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when (medication.frequency) {
                        MedicationFrequency.DAILY -> "매일"
                        MedicationFrequency.EVERY_OTHER_DAY -> "격일"
                        MedicationFrequency.SPECIFIC_DAYS -> "특정 요일"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Switch(
                    checked = medication.isEnabled,
                    onCheckedChange = { onToggle() }
                )
            }
        }
    }
}
