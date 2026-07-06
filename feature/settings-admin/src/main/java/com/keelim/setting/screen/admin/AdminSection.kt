package com.keelim.setting.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space32
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

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

        val textState = rememberTextFieldState()
        KuiFilledTextField(
            textState,
            isError = isError,
            label = { KuiText(stringResource(Res.string.settings_admin_deeplink_label)) },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    KuiIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(Res.string.common_action_clear),
                        modifier = Modifier.clickable {
                            setText("")
                        },
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            onKeyboardAction = {
                KeyboardActions(
                    onDone = {
                        if (text.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onClick(text)
                        }
                    },
                )
            }
        )

        KuiIcon(
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
            contentDescription = stringResource(Res.string.common_action_search),
        )
    }
}

@Preview
@Composable
private fun PreviewSchemeTestSection() {
    SchemeTestSection(onClick = {})
}
