@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.keelim.setting.screen.settings

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.ThumbUp
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.setting.BuildConfig
import com.keelim.setting.di.DeviceInfo
import com.keelim.shared.data.UserState

data class Category(
    val title: String,
    val icon: ImageVector,
    val visible: Boolean = true,
    val onClick: () -> Unit = {},
    val onLongClick: () -> Unit = {},
)

@Composable
fun SettingsRoute(
    onThemeChangeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAlarmsClick: () -> Unit,
    onFaqClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
    onAppUpdateClick: () -> Unit,
    onAdminClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onNotificationsClick = onNotificationsClick,
        onAlarmsClick = onAlarmsClick,
        onLabClick = onLabClick,
        onAppUpdateClick = onAppUpdateClick,
        onFaqClick = onFaqClick,
        onOpenSourceClick = onOpenSourceClick,
        onThemeChangeClick = onThemeChangeClick,
        onAdminClick = onAdminClick,
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onNotificationsClick: () -> Unit,
    onAlarmsClick: () -> Unit,
    onLabClick: () -> Unit,
    onFaqClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onThemeChangeClick: () -> Unit,
    onAppUpdateClick: () -> Unit,
    onAdminClick: () -> Unit,
) {
    when (uiState) {
        is SettingsUiState.Initialized -> EmptyView()
        is SettingsUiState.Success -> {
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
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Go back",
                                )
                            }
                        },
                        actions = {},
                    )
                },
            ) { padding ->
                var clicked by remember { mutableStateOf(false) }
                val items = remember {
                    listOf(
                        Category(
                            title = "공지사항",
                            icon = Icons.Outlined.Check,
                            onClick = onNotificationsClick,
                        ),
                        Category(
                            title = "알림내역",
                            icon = Icons.Outlined.Notifications,
                            onClick = onAlarmsClick,
                            onLongClick = {
                                clicked = true
                            },
                        ),
                        Category(title = "실험실", icon = Icons.Outlined.Lock, onClick = onLabClick),
                        Category(
                            title = "앱 업데이트",
                            icon = Icons.Rounded.ThumbUp,
                            onClick = onAppUpdateClick,
                        ),
                        Category(
                            title = "FAQ",
                            icon = Icons.Rounded.KeyboardArrowUp,
                            onClick = onFaqClick,
                        ),
                        Category(
                            title = "OpenSource",
                            icon = Icons.AutoMirrored.Outlined.List,
                            onClick = onOpenSourceClick,
                        ),
                        Category(
                            title = "Theme Change",
                            icon = Icons.Rounded.ArrowDropDown,
                            onClick = onThemeChangeClick,
                        ),
                        Category(
                            title = "Admin",
                            icon = Icons.Rounded.Lock,
                            visible = BuildConfig.DEBUG,
                            onClick = onAdminClick,
                        ),
                        Category(
                            title = "App Version: ${uiState.deviceInfo.versionName}",
                            icon = Icons.Outlined.Build,
                        ),
                        Category(
                            title = "${uiState.userState.visitedTime} 번 방문하셨습니다.",
                            icon = Icons.Outlined.ThumbUp,
                        ),
                    )
                }
                LazyColumn(
                    contentPadding = padding,
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(space12),
                ) {
                    items(
                        items = items,
                        key = { it.title },
                    ) { item ->
                        CategoryItem(
                            title = item.title,
                            icon = item.icon,
                            onClick = item.onClick,
                        )
                    }
                    item {
                        AnimatedVisibility(clicked) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .padding(horizontal = space8, vertical = space4)
                                    .clip(RoundedCornerShape(space4))
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                    ),
                            ) {
                                Text(
                                    text = "FCM Token: ${uiState.fcmToken}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = space8, vertical = space4)
                                .clip(RoundedCornerShape(space4))
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Platform: ${uiState.deviceInfo.platform}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        uiState = SettingsUiState.Success(
            userState = UserState(),
            deviceInfo = DeviceInfo(
                deviceName = "Ned Pruitt",
                deviceBrand = "dolore",
                deviceModel = "tale",
                versionName = "2023.12.25",
                platform = "nonumes",
                isSupported = false,
            ),
            fcmToken = "hello this fcm token",
        ),
        onFaqClick = {},
        onThemeChangeClick = {},
        onNotificationsClick = {},
        onOpenSourceClick = {},
        onLabClick = {},
        onAppUpdateClick = {},
        onAlarmsClick = {},
        onAdminClick = {},
    )
}

@Composable
fun CategoryItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onLongClick: () -> Unit = {},
) {
    AnimatedVisibility(visible) {
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
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        clicked = true
                        awaitRelease()
                        clicked = false
                    })
                }
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
            shape = MaterialTheme.shapes.medium,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = space16, vertical = space16)
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
}
