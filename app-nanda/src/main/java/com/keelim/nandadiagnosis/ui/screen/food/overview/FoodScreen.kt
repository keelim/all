package com.keelim.nandadiagnosis.ui.screen.food.overview

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.shared.data.database.model.FoodEntity
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodRoute(
    onEditClick: (String) -> Unit,
    viewModel: FoodViewModel = hiltViewModel(),
) {
    val foods by viewModel.todayFoods.collectAsStateWithLifecycle()
    val totalCalories by viewModel.todayTotalCalories.collectAsStateWithLifecycle()

    FoodScreen(
        foods = foods,
        totalCalories = totalCalories,
        onAddFood = viewModel::addFood,
        onDeleteFood = viewModel::deleteFood,
    )
}

@Composable
fun FoodScreen(
    foods: List<FoodEntity>,
    totalCalories: Int,
    onAddFood: (String, String) -> Unit,
    onDeleteFood: (Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    KuiScaffold(
        floatingActionButton = {
            KuiFloatingActionButton(onClick = { showDialog = true }) {
                KuiIcon(imageVector = Icons.Default.Add, contentDescription = stringResource(Res.string.nanda_food_add_description))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            KuiCard(padded = false,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KuiText(
                        text = stringResource(Res.string.nanda_food_total_calories_today),
                        style = KuiTheme.typography.titleMedium,
                        color = KuiTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    KuiText(
                        text = stringResource(Res.string.nanda_food_calories_value, totalCalories.toUiNumber()),
                        style = KuiTheme.typography.displayMedium,
                        color = KuiTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Food List
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foods, key = { it.id }) { food ->
                    FoodItem(food = food, onDelete = { onDeleteFood(food.id) })
                }
            }
        }
    }

    if (showDialog) {
        AddFoodDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, calories ->
                onAddFood(title, calories)
                showDialog = false
            }
        )
    }
}

@Composable
fun FoodItem(
    food: FoodEntity,
    onDelete: () -> Unit
) {
    KuiCard(padded = false,
        modifier = Modifier.fillMaxWidth()
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
                    text = food.title,
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurface
                )
                KuiText(
                    text = stringResource(Res.string.nanda_food_calories_value, food.calories.toUiNumber()),
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant
                )
            }
            KuiIconButton(onClick = onDelete) {
                KuiIcon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.common_action_delete),
                    tint = KuiTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    KuiAlertDialog(
        onDismissRequest = onDismiss,
        title = { KuiText(text = stringResource(Res.string.nanda_food_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                KuiOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { KuiText(stringResource(Res.string.nanda_food_name_label)) },
                    singleLine = true
                )
                KuiOutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { KuiText(stringResource(Res.string.nanda_food_calories_label)) },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            KuiButton(
                onClick = { onConfirm(title, calories) },
                enabled = title.isNotBlank() && calories.toIntOrNull() != null
            ) {
                KuiText(stringResource(Res.string.common_action_add))
            }
        },
        dismissButton = {
            KuiTextButton(onClick = onDismiss) {
                KuiText(stringResource(Res.string.common_action_cancel))
            }
        }
    )
}
