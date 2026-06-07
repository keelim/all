package com.keelim.arducon.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.StringResource

internal enum class ArduconToolAction {
    Playground,
    QrScanner,
    SchemeSearch,
    OgTagPreview,
    JsonFormatter,
    Base64,
    UrlShortener,
    DeviceInfo,
    Saastatus,
    Stats,
}

internal data class ArduconToolGroup(
    val title: StringResource,
    val items: List<ArduconToolItem>,
)

internal data class ArduconToolItem(
    val title: StringResource,
    val description: StringResource,
    val badge: StringResource,
    val icon: ImageVector,
    val action: ArduconToolAction,
)

internal val arduconToolGroups = listOf(
    ArduconToolGroup(
        title = Res.string.arducon_tool_group_link_test,
        items = listOf(
            ArduconToolItem(
                title = Res.string.playground_title,
                description = Res.string.arducon_tool_playground_desc,
                badge = Res.string.arducon_tool_badge_integrated,
                icon = Icons.Default.Build,
                action = ArduconToolAction.Playground,
            ),
            ArduconToolItem(
                title = Res.string.arducon_main_qr_code_scanner,
                description = Res.string.arducon_tool_qr_scanner_desc,
                badge = Res.string.arducon_tool_badge_scan,
                icon = Icons.Default.AddCircle,
                action = ArduconToolAction.QrScanner,
            ),
            ArduconToolItem(
                title = Res.string.arducon_search_title,
                description = Res.string.arducon_tool_scheme_search_desc,
                badge = Res.string.arducon_tool_badge_scheme,
                icon = Icons.Default.Search,
                action = ArduconToolAction.SchemeSearch,
            ),
            ArduconToolItem(
                title = Res.string.arducon_main_og_tag_preview,
                description = Res.string.arducon_tool_og_tag_desc,
                badge = Res.string.arducon_tool_badge_preview,
                icon = Icons.Default.ThumbUp,
                action = ArduconToolAction.OgTagPreview,
            ),
        ),
    ),
    ArduconToolGroup(
        title = Res.string.arducon_tool_group_data_tools,
        items = listOf(
            ArduconToolItem(
                title = Res.string.arducon_json_formatter_title,
                description = Res.string.arducon_tool_json_formatter_desc,
                badge = Res.string.arducon_tool_badge_format,
                icon = Icons.Default.Create,
                action = ArduconToolAction.JsonFormatter,
            ),
            ArduconToolItem(
                title = Res.string.arducon_tool_base64_title,
                description = Res.string.arducon_tool_base64_desc,
                badge = Res.string.arducon_tool_badge_encode,
                icon = Icons.Default.Lock,
                action = ArduconToolAction.Base64,
            ),
            ArduconToolItem(
                title = Res.string.arducon_url_shortener_title,
                description = Res.string.arducon_tool_url_shortener_desc,
                badge = Res.string.arducon_tool_badge_link,
                icon = Icons.Default.List,
                action = ArduconToolAction.UrlShortener,
            ),
        ),
    ),
    ArduconToolGroup(
        title = Res.string.arducon_tool_group_app_environment,
        items = listOf(
            ArduconToolItem(
                title = Res.string.arducon_tool_device_info_title,
                description = Res.string.arducon_tool_device_info_desc,
                badge = Res.string.arducon_tool_badge_device,
                icon = Icons.Default.Info,
                action = ArduconToolAction.DeviceInfo,
            ),
            ArduconToolItem(
                title = Res.string.arducon_tool_saastatus_title,
                description = Res.string.arducon_tool_saastatus_desc,
                badge = Res.string.arducon_tool_badge_status,
                icon = Icons.Default.AddCircle,
                action = ArduconToolAction.Saastatus,
            ),
            ArduconToolItem(
                title = Res.string.arducon_tool_stats_title,
                description = Res.string.arducon_tool_stats_desc,
                badge = Res.string.arducon_tool_badge_history,
                icon = Icons.Default.List,
                action = ArduconToolAction.Stats,
            ),
        ),
    ),
)
