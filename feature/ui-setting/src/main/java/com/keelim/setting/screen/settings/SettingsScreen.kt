@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.keelim.setting.screen.settings

import android.net.Uri
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
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.setting.BuildConfig
import com.keelim.shared.data.UserState
import com.keelim.web.navigateToWebModule

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
                            title = "Device Info",
                            icon = Icons.Outlined.Build,
                            onClick = onDeviceInfoClick,
                        ),
                    )
                }
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
                                    context.navigateToWebModule(Uri.parse(service.actionUrl))
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
        shape = MaterialTheme.shapes.medium,
        color = if (service.actionUrl.isBlank()) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer,
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (service.actionUrl.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
