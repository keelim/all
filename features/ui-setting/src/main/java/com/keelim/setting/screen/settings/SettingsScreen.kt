@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.setting.screen.settings

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.setting.di.DeviceInfo

@Composable
fun SettingsRoute(
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()
    SettingsScreen(
        deviceInfo = deviceInfo,
        onNotificationsClick = onNotificationsClick,
        onOpenSourceClick = onOpenSourceClick,
        onLabClick = onLabClick,
    )
}

@Composable
fun SettingsScreen(
    deviceInfo: DeviceInfo,
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val hasScrolled by remember { derivedStateOf { listState.firstVisibleItemScrollOffset > 0 } }
    val appBarElevation by
        animateDpAsState(
            targetValue =
            if (hasScrolled) {
                4.dp
            } else {
                0.dp
            },
            label = "",
        )
    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current) { "this is not null" }
            .onBackPressedDispatcher
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor =
                    if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = if (hasScrolled) 1f else 0f,
                        )
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                ),
                modifier = Modifier.shadow(appBarElevation),
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {},
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                CategoryItem(
                    title = "Notifications",
                    icon = Icons.Outlined.Notifications,
                    onClick = onNotificationsClick,
                )
            }
            item {
                CategoryItem(
                    title = "OpenSource",
                    icon = Icons.Outlined.List,
                    onClick = onOpenSourceClick,
                )
            }
            item {
                CategoryItem(
                    title = "실험실",
                    icon = Icons.Outlined.Lock,
                    onClick = onLabClick,
                )
            }
            if (deviceInfo.versionName != null) {
                item {
                    CategoryItem(
                        title = "App Version: ${deviceInfo?.versionName}",
                        icon = Icons.Outlined.Build,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        deviceInfo = DeviceInfo(
            deviceName = "Ned Pruitt",
            deviceBrand = "dolore",
            deviceModel = "tale",
            versionName = null,
        ),
        onNotificationsClick = {},
        onOpenSourceClick = {},
        onLabClick = {},
    )
}

@Composable
fun CategoryItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    var clicked by remember { mutableStateOf(false) }
    val sizeScale by animateFloatAsState(
        targetValue = if (clicked) .9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "",
    )
    Surface(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    clicked = true
                    awaitRelease()
                    clicked = false
                })
            },
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .scale(sizeScale),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
