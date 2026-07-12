package com.keelim.setting.screen.admin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
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
                    KuiIconButton(
                        onClick = { setText("") },
                        modifier = Modifier.size(KuiTheme.spacing.componentLg),
                    ) {
                        KuiIcon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.common_action_clear),
                            modifier = Modifier.size(KuiTheme.spacing.space6),
                        )
                    }
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

        KuiIconButton(
            onClick = {
                if (text.isEmpty()) {
                    setError(true)
                } else {
                    setError(false)
                    onClick(text)
                }
            },
            modifier = Modifier.size(KuiTheme.spacing.componentLg),
        ) {
            KuiIcon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(Res.string.common_action_search),
                modifier = Modifier.size(KuiTheme.spacing.space6),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSchemeTestSection() {
    SchemeTestSection(onClick = {})
}
