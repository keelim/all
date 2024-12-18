package com.keelim.nandadiagnosis.ui.screen.food.overview


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FoodRoute(
    onEditClick: (String) -> Unit,
    viewModel: FoodViewModel = hiltViewModel(),
) {
    FoodScreen(
        onEditClick = onEditClick,
    )
}

@Composable
fun FoodScreen(
    onEditClick: (String) -> Unit,
) {

}


@Preview
@Composable
private fun PreviewFoodScreen() {
    FoodScreen(
        onEditClick = {},
    )
}
