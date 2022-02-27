package com.keelim.compose.ui.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


data class ItemViewState(
    val text:String
)

@Composable
fun MySampleList(
    modifier: Modifier = Modifier,
    itemViewStates: List<ItemViewState>
){
    
}

@Composable
fun MyItem(state: ItemViewState){
    Text(text = state.text)
}