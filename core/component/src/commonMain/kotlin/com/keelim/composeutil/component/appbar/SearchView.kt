package com.keelim.composeutil.component.appbar

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiIconButton
import com.keelim.composeutil.component.kui.KuiMaterialTheme
import com.keelim.composeutil.component.kui.KuiText
import com.keelim.composeutil.component.kui.KuiTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SearchView(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuiTextField(
        value = query,
        onValueChange = { newQuery ->
            onQueryChanged(newQuery)
        },
        leadingIcon = {
            KuiIcon(
                imageVector = Icons.Rounded.Search,
                tint = KuiMaterialTheme.colorScheme.background,
                contentDescription = null,
            )
        },
        trailingIcon = {
            KuiIconButton(onClick = {
                onClearQuery()
            }) {
                KuiIcon(
                    imageVector = Icons.Rounded.Clear,
                    tint = KuiMaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                )
            }
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(),
        placeholder = { KuiText(text = "search") },
        textStyle = KuiMaterialTheme.typography.labelLarge,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = modifier,
    )
}
