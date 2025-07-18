/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.composeutil.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        placeholder = {
            Text(text = "Search Bar")
        },
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}

@Composable
fun AlignYourBodyElement(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Sample")
    }
}

@Preview
@Composable
fun AlignYourBodyElementPreview() {
    AlignYourBodyElement()
}

@Composable
fun FavoriteCollectionCard(
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(192.dp),
        ) {
            Text(
                text = "Sample",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = space16),
            )
        }
    }
}

@Preview
@Composable
fun FavoriteCollectionCardPreview() {
    FavoriteCollectionCard()
}

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space8),
        contentPadding = PaddingValues(horizontal = space16),
    ) {
        items(listOf(1, 2, 3)) { item ->
            AlignYourBodyElement()
        }
    }
}

@Preview
@Composable
fun AlignYourBodyRowPreview() {
    AlignYourBodyRow()
}

@Composable
fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(
                    top = 40.dp,
                    bottom = space8,
                )
                .padding(horizontal = space16),
        )
        content.invoke()
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSection(title = "안녕하세요") {
        AlignYourBodyRow()
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(space16),
    ) {
        Spacer(Modifier.height(space16))
        SearchBar(Modifier.padding(horizontal = space16))
        HomeSection(title = "안녕하세요") {
            AlignYourBodyRow()
        }

        HomeSection(title = "반갑습니다.") {
            AlignYourBodyRow()
        }
        Spacer(Modifier.height(space16))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
