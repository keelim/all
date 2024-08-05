@file:OptIn(ExperimentalLayoutApi::class)

package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryStateSection(
    uiState: CategoryState,
    onCategoryClick: (Int, String) -> Unit,
) = trace("CategoryStateSection") {
    when (uiState) {
        CategoryState.Error,
        CategoryState.Empty,
        -> EmptyView()

        CategoryState.Loading -> Loading()
        is CategoryState.Success -> Categories(
            uiState.items,
            onCategoryClick,
        )
    }
}

@Composable
private fun Categories(
    items: List<String>,
    onCategoryClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("categories") {
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(space8),
    ) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.bodyMedium,
        )
        FlowRow(
            maxItemsInEachRow = 3,
        ) {
            val itemModifier =
                Modifier
                    .height(100.dp)
                    .padding(space8)
                    .clip(RoundedCornerShape(space8))
            items.fastForEachIndexed { index, item ->
                CategoryCard(
                    index = index,
                    categoryTitle = item,
                    onCategoryClick = onCategoryClick,
                    modifier = itemModifier,
                )
            }
        }

    }
}

@Composable
private fun CategoryCard(
    index: Int,
    categoryTitle: String,
    onCategoryClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("CategoryCard") {
    Card(
        modifier = modifier.clip(RoundedCornerShape(space8)),
        elevation = CardDefaults.cardElevation(defaultElevation = space4),
        onClick = { onCategoryClick(index + 1, categoryTitle) },
    ) {
        Column(
            modifier = modifier.padding(space8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = categoryTitle,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCategories() {
    Categories(
        modifier = Modifier.fillMaxWidth(),
        items = persistentListOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "efghijklmnop",
        ),
        onCategoryClick = { _, _ -> },
    )
}

@Preview
@Composable
private fun PreviewCategoryCard() {
    CategoryCard(
        categoryTitle = "병명",
        onCategoryClick = { _, _ -> },
        modifier = Modifier,
        index = 4713
    )
}
