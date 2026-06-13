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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.common.web.BrowserLauncher
import com.keelim.common.web.NoOpBrowserLauncher
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.Res
import com.keelim.core.resource.settings_back_description
import com.keelim.core.resource.settings_category_admin
import com.keelim.core.resource.settings_category_app_update
import com.keelim.core.resource.settings_category_device_info
import com.keelim.core.resource.settings_category_faq
import com.keelim.core.resource.settings_category_lab
import com.keelim.core.resource.settings_category_notice
import com.keelim.core.resource.settings_category_notification_history
import com.keelim.core.resource.settings_category_open_source
import com.keelim.core.resource.settings_category_theme_change
import com.keelim.core.resource.settings_fcm_token
import com.keelim.core.resource.settings_title
import android.content.pm.ApplicationInfo
import com.keelim.shared.data.UserState
import org.jetbrains.compose.resources.stringResource

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
    onDeviceInfoClick: () -> Unit,
    browserLauncher: BrowserLauncher,
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
        onDeviceInfoClick = onDeviceInfoClick,
        browserLauncher = browserLauncher,
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
    onDeviceInfoClick: () -> Unit,
    browserLauncher: BrowserLauncher,
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
            val context = LocalContext.current
            val settingsTitle = stringResource(Res.string.settings_title)
            val settingsBackDescription = stringResource(Res.string.settings_back_description)
            val settingsNotice = stringResource(Res.string.settings_category_notice)
            val settingsNotificationHistory =
                stringResource(Res.string.settings_category_notification_history)
            val settingsLab = stringResource(Res.string.settings_category_lab)
            val settingsAppUpdate = stringResource(Res.string.settings_category_app_update)
            val settingsFaq = stringResource(Res.string.settings_category_faq)
            val settingsOpenSource = stringResource(Res.string.settings_category_open_source)
            val settingsThemeChange = stringResource(Res.string.settings_category_theme_change)
            val settingsAdmin = stringResource(Res.string.settings_category_admin)
            val settingsDeviceInfo = stringResource(Res.string.settings_category_device_info)
            val isDebuggable = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

            Scaffold(
                containerColor = KuiTheme.colorScheme.surface,
                contentColor = KuiTheme.colorScheme.onSurface,
                topBar = {
                    CenterAlignedTopAppBar(
                        colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor =
                            if (isSystemInDarkTheme()) {
                                KuiTheme.colorScheme.surfaceVariant.copy(
                                    alpha = if (hasScrolled) 1f else 0f,
                                )
                            } else {
                                KuiTheme.colorScheme.surface
                            },
                        ),
                        modifier = Modifier.shadow(appBarElevation),
                        title = { Text(text = settingsTitle) },
                        navigationIcon = {
                            IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = settingsBackDescription,
                                )
                            }
                        },
                        actions = {},
                    )
                },
            ) { padding ->
                var clicked by remember { mutableStateOf(false) }
                val items = listOf(
                    Category(
                        title = settingsNotice,
                        icon = Icons.Outlined.Check,
                        onClick = onNotificationsClick,
                    ),
                    Category(
                        title = settingsNotificationHistory,
                        icon = Icons.Outlined.Notifications,
                        onClick = onAlarmsClick,
                        onLongClick = {
                            clicked = true
                        },
                    ),
                    Category(title = settingsLab, icon = Icons.Outlined.Lock, onClick = onLabClick),
                    Category(
                        title = settingsAppUpdate,
                        icon = Icons.Rounded.ThumbUp,
                        onClick = onAppUpdateClick,
                    ),
                    Category(
                        title = settingsFaq,
                        icon = Icons.Rounded.KeyboardArrowUp,
                        onClick = onFaqClick,
                    ),
                    Category(
                        title = settingsOpenSource,
                        icon = Icons.AutoMirrored.Outlined.List,
                        onClick = onOpenSourceClick,
                    ),
                    Category(
                        title = settingsThemeChange,
                        icon = Icons.Rounded.ArrowDropDown,
                        onClick = onThemeChangeClick,
                    ),
                    Category(
                        title = settingsAdmin,
                        icon = Icons.Rounded.Lock,
                        visible = isDebuggable,
                        onClick = onAdminClick,
                    ),
                    Category(
                        title = settingsDeviceInfo,
                        icon = Icons.Outlined.Build,
                        onClick = onDeviceInfoClick,
                    ),
                )
                LazyColumn(
                    contentPadding = padding,
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(space12),
                ) {
                    item {
                        FamilyServiceCarousel(
                            services = uiState.familyServices,
                            onServiceClick = { service ->
                                if (service.actionUrl.isNotBlank()) {
                                    browserLauncher.open(service.actionUrl)
                                }
                            },
                        )
                    }
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
                                        color = KuiTheme.colorScheme.primaryContainer,
                                    ),
                            ) {
                                Text(
                                    text = stringResource(Res.string.settings_fcm_token, uiState.fcmToken),
                                    fontWeight = FontWeight.Bold,
                                    style = KuiTheme.typography.bodyLarge,
                                )
                            }
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
        onDeviceInfoClick = {},
        browserLauncher = NoOpBrowserLauncher,
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
            shape = KuiTheme.shapes.medium,
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
                    tint = KuiTheme.colorScheme.onSurface,
                )
                Text(title, style = KuiTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun FamilyServiceCarousel(
    services: List<FamilyService>,
    onServiceClick: (FamilyService) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (services.isEmpty()) return
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { services.count() },
        preferredItemWidth = 200.dp,
        itemSpacing = space8,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = space8),
    ) { i ->
        val service = services[i]
        FamilyServiceItem(
            service = service,
            onClick = { onServiceClick(service) }
        )
    }
}

@Composable
fun FamilyServiceItem(
    service: FamilyService,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = KuiTheme.shapes.medium,
        color = if (service.actionUrl.isBlank()) KuiTheme.colorScheme.surfaceVariant else KuiTheme.colorScheme.primaryContainer,
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(space16)
        ) {
            Text(
                text = service.title,
                style = KuiTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (service.actionUrl.isBlank()) KuiTheme.colorScheme.onSurfaceVariant else KuiTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
