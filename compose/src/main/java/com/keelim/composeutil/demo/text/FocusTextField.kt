@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
)

package com.keelim.composeutil.demo.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.layout.Column

@Composable
fun FocusTextField() {
    Column {
        val focusManager = LocalFocusManager.current
        var value by remember { mutableStateOf("") }
        TextField(
            value = value,
            onValueChange = {
                value = it
            },
            singleLine = true,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
        )

        TextField(
            value = value,
            onValueChange = {
                value = it
            },
            singleLine = true,
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FocusTextViewPreview() {
    FocusTextField()
}

@Composable
fun FocusScreen() {
    Row {
        val focusManager = LocalFocusManager.current
        Column {
            val (a, b, c) = FocusRequester.createRefs()
            TextField(
                modifier = Modifier
                    .focusRequester(a)
                    .focusProperties {
                        next = b
                    },
                value = "",
                onValueChange = {},
            )
            TextField(
                modifier = Modifier
                    .focusRequester(b)
                    .focusProperties {
                        previous = a
                        next = c
                    },
                value = "",
                onValueChange = {},
            )
            TextField(
                modifier = Modifier
                    .focusRequester(c)
                    .focusProperties {
                        previous = b
                    },
                value = "",
                onValueChange = {},
            )
        }
        Column {
            Button(onClick = {
                focusManager.moveFocus(FocusDirection.Previous)
            }) {
                Text("previous")
            }
            Button(onClick = {
                focusManager.moveFocus(FocusDirection.Next)
            }) {
                Text("next")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FocusScreenPreview() {
    FocusScreen()
}
