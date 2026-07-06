package com.keelim.composeutil.component.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiIconButton
import com.keelim.composeutil.component.kui.KuiOutlinedTextField
import com.keelim.composeutil.component.kui.KuiText
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
        KuiOutlinedTextField(
            value = input,
            onValueChange = setInput,
            leadingIcon = { KuiText(text = "#", fontWeight = FontWeight.Bold) },
            placeholder = { KuiText(text = "hashtag 를 입력해주세요") },
            trailingIcon = {
                AnimatedVisibility(
                    visible = input.isNotEmpty() && isValid,
                ) {
                    KuiIconButton(onClick = {
                        setInput("")
                        onAdd(input)
                    }) {
                        KuiIcon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                    }
                }
            },
            isError = isValid.not(),
        )
    }
    AnimatedVisibility(
        visible = isValid.not(),
    ) {
        KuiText(
            text = "not valid hashTag",
        )
    }
}
