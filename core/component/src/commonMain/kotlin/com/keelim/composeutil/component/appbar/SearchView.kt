package com.keelim.composeutil.component.appbar

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchView(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = { newQuery ->
            onQueryChanged(newQuery)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                onClearQuery()
            }) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                )
            }
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(),
        placeholder = { Text(text = "search") },
        textStyle = MaterialTheme.typography.labelLarge,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    SearchView(query = "Test", onQueryChanged = { }, onSearch = { }, onClearQuery = { })
}
