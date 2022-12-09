package com.keelim.compose.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.compose.BuildConfig

sealed class SettingAction {
    object AlarmSetting : SettingAction()
    object ThemeSetting : SettingAction()
    object Lab : SettingAction()
    object OtherApp : SettingAction()
    object ShowLogcat: SettingAction()
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    appName: String = "",
    clickAction: ((SettingAction) -> Unit)? = null,
    developerModeDataHolder: Map<String, String>? = null,
) {
    Surface(
        modifier = modifier
            .padding(
                horizontal = 8.dp,
                vertical = 16.dp,
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$appName 환경설정",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.clickable {
                    clickAction?.invoke(SettingAction.AlarmSetting)
                },
                text = "알림 설정",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.clickable {
                    clickAction?.invoke(SettingAction.ThemeSetting)
                },
                text = "테마 설정",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.clickable {
                    clickAction?.invoke(SettingAction.Lab)
                },
                text = "실험실",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        clickAction?.invoke(SettingAction.OtherApp)
                    },
                text = "개발자가 만든 다른 앱",
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray,
            )
            if (BuildConfig.DEBUG) {
                Text(
                    text = "개발자 옵션",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.height(2.dp))
                LazyColumn {
                    items(developerModeDataHolder?.toList() ?: emptyList()) { (key, value) ->
                        DeveloperOptionPart(
                            displayTitle = key,
                            displayKey = key,
                            value = developerModeDataHolder?.get(key)
                        )
                    }
                }
                DeveloperOptionPart(
                    modifier = Modifier.clickable {
                        clickAction?.invoke(SettingAction.ShowLogcat)
                    },
                    displayTitle = "로그캣 확인하기",
                    displayKey = "logcat",
                    value = ""
                )
            }
        }
    }
}

@Composable
fun DeveloperOptionPart(
    modifier: Modifier = Modifier,
    displayTitle: String,
    displayKey: String,
    value: String? = null
) {
    Spacer(modifier = modifier.height(16.dp))
    Text(
        text = displayTitle,
        style = MaterialTheme.typography.titleSmall,
    )
    Text(
        text = "$displayKey: $value",
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray
    )
    Spacer(modifier = modifier.height(4.dp))
    Divider(
        color = Color.DarkGray,
        thickness = 1.dp,
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
            }
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
                value = "1234"
            )
        }
    }
}
