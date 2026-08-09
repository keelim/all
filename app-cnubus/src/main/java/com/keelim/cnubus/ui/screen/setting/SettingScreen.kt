/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keelim.cnubus.ui.screen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.keelim.core.resource.Res
import com.keelim.core.resource.cnubus_settings_app
import com.keelim.core.resource.cnubus_settings_homepage
import com.keelim.core.resource.cnubus_settings_map_shortcut
import com.keelim.core.resource.cnubus_tab_settings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingScreen(
    onScreenAction: (ScreenAction) -> Unit,
) = trace("SettingScreen") {
    val spacing = KuiTheme.spacing
    val appSettingsLabel = stringResource(Res.string.cnubus_settings_app)
    val homepageLabel = stringResource(Res.string.cnubus_settings_homepage)
    val mapShortcutLabel = stringResource(Res.string.cnubus_settings_map_shortcut)
    val data = remember(appSettingsLabel, homepageLabel, mapShortcutLabel) {
        listOf(
            Setting(
                text = appSettingsLabel,
                action = ScreenAction.AppSetting,
                icon = Icons.Default.Settings,
            ),
            Setting(
                text = homepageLabel,
                action = ScreenAction.Homepage,
                icon = Icons.Default.Home,
            ),
            Setting(
                text = mapShortcutLabel,
                action = ScreenAction.Map,
                icon = Icons.Default.LocationOn,
            ),
        )
    }
    KuiScaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = spacing.cardPadding,
                        vertical = spacing.sectionGap,
                    ),
            ) {
                KuiText(
                    text = stringResource(Res.string.cnubus_tab_settings),
                    style = KuiTheme.typography.headlineSmall,
                    color = KuiTheme.colorScheme.onSurface,
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(
                items = data,
                key = { it.action },
                itemContent = { item ->
                    SettingItem(item = item, onScreenAction)
                },
            )
        }
    }
}

@Stable
data class Setting(
    val text: String,
    val action: ScreenAction,
    val icon: ImageVector,
)

@Composable
fun SettingItem(
    item: Setting,
    onScreenAction: (ScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) = trace("SettingItem") {
    KuiCard(padded = false,
        modifier = modifier
            .padding(
                horizontal = KuiTheme.spacing.space2,
                vertical = KuiTheme.spacing.space1,
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.surface),
    ) {
        CellItem(
            text = item.text,
            icon = item.icon,
            onClick = { onScreenAction(item.action) },
        )
    }
}

@Composable
private fun CellItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("CellItem") {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = KuiTheme.spacing.componentLg)
            .clickable { onClick() }
            .padding(
                horizontal = KuiTheme.spacing.cardPadding,
                vertical = KuiTheme.spacing.space2,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuiIcon(
            imageVector = icon,
            contentDescription = null,
            tint = KuiTheme.colorScheme.primary,
            modifier = Modifier.padding(end = KuiTheme.spacing.sectionGap),
        )
        KuiText(
            text = text,
            style = KuiTheme.typography.titleMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun PreviewSettingScreen() {
    SettingScreen(
        onScreenAction = {},
    )
}
