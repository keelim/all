@file:OptIn(ExperimentalLayoutApi::class)

package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryStateSection(
    uiState: CategoryState,
    onCategoryClick: (Int, String) -> Unit,
    onEditTypeClick: (CategoriesType) -> Unit,
    onMedicationClick: () -> Unit = {},
) = trace("CategoryStateSection") {
    when (uiState) {
        CategoryState.Error,
        CategoryState.Empty,
        -> EmptyView()

        CategoryState.Loading -> Loading()
        is CategoryState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(space12), // Outer padding
                verticalArrangement = Arrangement.spacedBy(space24),
            ) {
                // 1. Hero Section: Medication
                MedicationEntryCard(
                    onMedicationClick = onMedicationClick,
                )

                // 2. Activity Section: Food & Exercise
                SectionCard(title = "나의 활동") {
                    Categories(
                        title = "식단 기록",
                        items = listOf(),
                        type = CategoriesType.FOOD,
                        onCategoryClick = { _, _ -> },
                        onEditTypeClick = onEditTypeClick,
                        emptyMessage = "오늘 먹은 음식을 기록해보세요.",
                        icon = Icons.Rounded.Add,
                    )
                    Spacer(modifier = Modifier.height(space16))
                    Categories(
                        title = "운동 기록",
                        items = listOf(),
                        type = CategoriesType.EXERCISE,
                        onCategoryClick = { _, _ -> },
                        onEditTypeClick = onEditTypeClick,
                        emptyMessage = "오늘 한 운동을 기록해보세요.",
                        icon = Icons.Rounded.Add,
                    )
                }

                // 3. Main Grid: NANDA Categories
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space12),
                ) {
                    Text(
                        text = "NANDA 진단 분류",
                        style = KuiTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.padding(start = space4),
                    )
                    Categories(
                        title = "NANDA", // Not used in grid mode effectively but kept for sig
                        items = uiState.items,
                        onCategoryClick = onCategoryClick,
                        type = CategoriesType.CATEGORY,
                        onEditTypeClick = onEditTypeClick,
                    )
                }
                Spacer(modifier = Modifier.height(space24))
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space12),
    ) {
        Text(
            text = title,
            style = KuiTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(start = space4),
        )
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(space16),
            colors = CardDefaults.outlinedCardColors(
                containerColor = KuiTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(space16),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun MedicationEntryCard(
    onMedicationClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("MedicationEntryCard") {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(space16),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        onClick = onMedicationClick,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            KuiTheme.colorScheme.primary,
                            KuiTheme.colorScheme.tertiary,
                        ),
                    ),
                )
                .padding(space24), // Increased padding
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space16),
            ) {
                Surface(
                    shape = CircleShape,
                    color = KuiTheme.colorScheme.surface.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize(),
                        tint = KuiTheme.colorScheme.onPrimary,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "복약 알림 설정",
                        style = KuiTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = KuiTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.height(space4))
                    Text(
                        text = "약 먹을 시간을 잊지 마세요!",
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = KuiTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

enum class CategoriesType {
    EXERCISE,
    FOOD,
    CATEGORY,
}

@Composable
private fun Categories(
    title: String,
    items: List<String>,
    type: CategoriesType,
    onCategoryClick: (Int, String) -> Unit,
    onEditTypeClick: (CategoriesType) -> Unit,
    modifier: Modifier = Modifier,
    emptyMessage: String = "데이터가 없습니다.",
    icon: ImageVector = Icons.AutoMirrored.Rounded.List,
) = trace("categories") {
    when (type) {
        CategoriesType.FOOD, CategoriesType.EXERCISE -> {
            Column(modifier = modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = space8),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = KuiTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                    IconButton(onClick = { onEditTypeClick(type) }) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
                    }
                }

                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(space12))
                            .background(KuiTheme.colorScheme.surfaceContainerHigh)
                            .clickable { onEditTypeClick(type) }
                            .padding(vertical = space24),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = KuiTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp),
                            )
                            Spacer(modifier = Modifier.height(space8))
                            Text(
                                text = emptyMessage,
                                style = KuiTheme.typography.bodyMedium,
                                color = KuiTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else {
                    items.fastForEachIndexed { index, item ->
                        ListItem(
                            headlineContent = { Text(item) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = KuiTheme.colorScheme.surfaceContainerLow,
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(space12))
                                .clickable { onCategoryClick(index + 1, item) },
                        )
                        Spacer(modifier = Modifier.height(space8))
                    }
                }
            }
        }

        CategoriesType.CATEGORY -> {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(space12),
                verticalArrangement = Arrangement.spacedBy(space12),
            ) {
                items.fastForEachIndexed { index, item ->
                    CategoryGridCard(
                        index = index,
                        categoryTitle = item,
                        onCategoryClick = onCategoryClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryGridCard(
    index: Int,
    categoryTitle: String,
    onCategoryClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Generate a consistent color based on index or just use a nice variety
    // For now, we use a subtle surface variant or a primary container
    val containerColor = KuiTheme.colorScheme.surfaceContainer

    Card(
        modifier = modifier
            .aspectRatio(1.4f)
            .clip(RoundedCornerShape(space16))
            .clickable { onCategoryClick(index + 1, categoryTitle) },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(space12),
        ) {
            // Background Decorative Circle
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(KuiTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

            Column(
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Text(
                    text = "${index + 1}",
                    style = KuiTheme.typography.labelLarge,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(space4))
                Text(
                    text = categoryTitle,
                    style = KuiTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = KuiTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCategories() {
    Column {
        Categories(
            title = "Category",
            modifier = Modifier.fillMaxWidth(),
            items = persistentListOf(
                "a",
                "b",
                "c",
                "d",
                "e",
                "efghijklmnop",
            ),
            type = CategoriesType.CATEGORY,
            onCategoryClick = { _, _ -> },
            onEditTypeClick = { },
        )

        Categories(
            title = "Category",
            modifier = Modifier.fillMaxWidth(),
            items = persistentListOf(
                "a",
                "b",
                "c",
                "d",
                "e",
                "efghijklmnop",
            ),
            type = CategoriesType.FOOD,
            onCategoryClick = { _, _ -> },
            onEditTypeClick = { },
        )

        Categories(
            title = "Category",
            modifier = Modifier.fillMaxWidth(),
            items = persistentListOf(),
            type = CategoriesType.CATEGORY,
            onCategoryClick = { _, _ -> },
            onEditTypeClick = { },
        )
    }
}
