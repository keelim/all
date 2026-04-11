package com.keelim.composeutil.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}

@Preview
@Composable
fun AlignYourBodyElementPreview() {
    AlignYourBodyElement()
}

@Preview
@Composable
fun FavoriteCollectionCardPreview() {
    FavoriteCollectionCard()
}

@Preview
@Composable
fun AlignYourBodyRowPreview() {
    AlignYourBodyRow()
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSection(title = "안녕하세요") {
        AlignYourBodyRow()
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
