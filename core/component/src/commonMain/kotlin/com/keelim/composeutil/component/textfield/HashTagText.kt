package com.keelim.composeutil.component.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HashTagTextField(
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (input, setInput) = remember { mutableStateOf("") }
    val isValid: Boolean by remember(input) {
        derivedStateOf {
            input.contains(" ").not()
        }
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = input,
            onValueChange = setInput,
            leadingIcon = { Text(text = "#", fontWeight = FontWeight.Bold) },
            placeholder = { Text(text = "hashtag 를 입력해주세요") },
            trailingIcon = {
                AnimatedVisibility(
                    visible = input.isNotEmpty() && isValid,
                ) {
                    IconButton(onClick = {
                        setInput("")
                        onAdd(input)
                    }) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                    }
                }
            },
            isError = isValid.not(),
        )
    }
    AnimatedVisibility(
        visible = isValid.not(),
    ) {
        Text(
            text = "not valid hashTag",
        )
    }
}
