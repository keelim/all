package com.keelim.setting.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space32


@Composable
fun SchemeTestSection(
    onClick: (uri: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (text, setText) = remember { mutableStateOf("") }
        val (isError, setError) = remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier.weight(1f),
            value = text,
            isError = isError,
            onValueChange = setText,
            label = { Text("please write your deeplink") },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            setText("")
                        },
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.isEmpty()) {
                        setError(true)
                    } else {
                        setError(false)
                        onClick(text)
                    }
                },
            ),
        )

        Icon(
            imageVector = Icons.Default.Search,
            modifier = Modifier
                .size(space32)
                .clickable {
                    if (text.isEmpty()) {
                        setError(true)
                    } else {
                        setError(false)
                        onClick(text)
                    }
                },
            contentDescription = "Search",
        )
    }
}

@Preview
@Composable
private fun PreviewSchemeTestSection() {
    SchemeTestSection(onClick = {})
}
