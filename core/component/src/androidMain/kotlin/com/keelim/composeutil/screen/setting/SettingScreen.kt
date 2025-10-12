package com.keelim.composeutil.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4

sealed interface SettingAction {
    data object AlarmSetting : SettingAction
    data object ThemeSetting : SettingAction
    data object Lab : SettingAction
    data object OtherApp : SettingAction
    data object ShowLogcat : SettingAction
}

@Composable
fun DeveloperOptionPart(
    displayTitle: String,
    displayKey: String,
    modifier: Modifier = Modifier,
    value: String? = null,
) {
    Spacer(modifier = modifier.height(space16))
    Text(
        text = displayTitle,
        style = MaterialTheme.typography.titleSmall,
    )
    Text(
        text = "$displayKey: $value",
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
    )
    Spacer(modifier = modifier.height(space4))
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.DarkGray,
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    MaterialTheme {
        SettingScreen(
            appName = "Compose",
            developerModeDataHolder = buildMap {
                put("uuid", "1234")
                put("android_version", "Q")
                put("device_model", "sm-g960n")
                put("device_name", "Samsung Galaxy S21")
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeveloperOptionPartPreview() {
    MaterialTheme {
        Column {
            DeveloperOptionPart(
                displayTitle = "개발자 옵션1",
                displayKey = "개발자 옵션2",
                value = "1234",
            )
        }
    }
}
