@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.nandadiagnosis.ui.screen.food.overview


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.model.Food

@Composable
internal fun FoodSuccessSection(
    items: Map<String, List<Food>>,
    onEditClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items.forEach { category, foods ->
            stickyHeader {
                FoodHeader(
                    title = category,
                    onEditClick = onEditClick,
                )
            }
            if (foods.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(space64),
                        text = "데이터가 없습니다.",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                items(foods) { food ->
                    FoodItem(
                        food = food,
                    )
                }
            }
        }
    }
}

@Composable
private fun FoodHeader(
    title: String,
    onEditClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = space12, horizontal = space8),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = space8, horizontal = space8),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit food",
                modifier = Modifier.clickable {
                    onEditClick(title)
                }
            )
        }
    }
}

@Composable
private fun FoodItem(
    food: Food,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                text = food.title,
            )
        },
        trailingContent = {
            Text(
                text = "${food.kcal} kcal",
                style = MaterialTheme.typography.labelSmall,
            )
        },
        supportingContent = {
            Text(
                text = food.description,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier,
    )
}


@Preview
@Composable
private fun PreviewFoodSuccessSection() {
    FoodSuccessSection(
        items = mapOf(
            "탄수 화물" to listOf(
                Food(
                    title = "밥",
                    description = "밥은 맛있어요",
                    category = "탄수화물",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "탄수화물",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "단백질",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "지방",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "기타",
                    kcal = 0,
                ),
            ),
            "지방" to listOf(),
            "단백질" to listOf(
                Food(
                    title = "밥",
                    description = "밥은 맛있어요",
                    category = "탄수화물",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "탄수화물",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "단백질",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "지방",
                    kcal = 0,
                ),
                Food(
                    title = "빵",
                    description = "빵은 맛있어요",
                    category = "기타",
                    kcal = 0,
                ),
            ),
        ),
        onEditClick = {},
    )
}

