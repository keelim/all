package com.keelim.setting.screen.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space8
import com.keelim.shared.data.ThemeType


@Composable
fun ThemeRoute(
    viewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeTypeState by viewModel.themeTypeState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

    ThemeScreen(
        themeTypeState = themeTypeState,
        onCurrentThemeClick = { viewModel.setDialogVisibility(true) },
        onDialogClick = { viewModel.setDialogVisibility(false) },
        onRadioClick = { viewModel.updateThemeType(it) },
    )
}

@Composable
fun ThemeScreen(
    themeTypeState: ThemeTypeState,
    onCurrentThemeClick: () -> Unit,
    onDialogClick: () -> Unit,
    onRadioClick: (ThemeType) -> Unit,
) {

    if (themeTypeState.isDialogVisible) {
        ThemeDialog(
            state = themeTypeState,
            onDialogClick = onDialogClick,
            onRadioClick = onRadioClick,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        CurrentThemeText(
            themeType = themeTypeState.selectedRadio,
            onCurrentThemeClick = onCurrentThemeClick,
        )
    }
}

@Composable
fun CurrentThemeText(
    themeType: ThemeType,
    onCurrentThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = remember(themeType) {
        when (themeType) {
            ThemeType.SYSTEM -> "💻"
            ThemeType.LIGHT -> "🌞"
            ThemeType.DARK -> "🌚"
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onCurrentThemeClick,
    ) {
        Text(
            text = "$icon Current Theme: $themeType",
            modifier = Modifier.padding(space8),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun ThemeDialog(
    state: ThemeTypeState,
    onDialogClick: () -> Unit,
    onRadioClick: (ThemeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDialogClick,
        modifier = modifier,
        title = { Text(text = "Choose Theme") },
        text = {
            RadioButtons(
                state = state,
                onRadioClick = onRadioClick,
            )
        },
        confirmButton = {
            Button(
                onClick = onDialogClick
            ) {
                Text("Confirm")
            }
        }
    )
}

@Composable
fun RadioButtons(
    state: ThemeTypeState,
    onRadioClick: (ThemeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        state.items.fastForEach {
            Row(
                modifier = Modifier.selectable(
                    selected = (it.value == state.selectedRadio),
                    onClick = { onRadioClick(it.value) }
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (it.value == state.selectedRadio),
                    onClick = { onRadioClick(it.value) }
                )
                Text(text = it.title)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen() {
    ThemeScreen(
        themeTypeState = ThemeTypeState(selectedRadio = ThemeType.SYSTEM),
        onCurrentThemeClick = { },
        onDialogClick = { },
        onRadioClick = { },
    )
}
