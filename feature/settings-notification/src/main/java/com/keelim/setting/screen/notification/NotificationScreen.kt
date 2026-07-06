@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.keelim.setting.screen.notification

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Check
import com.keelim.core.designsystem.component.KuiCard
import com.keelim.core.designsystem.component.KuiCenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationRoute() {
    NotificationScreen()
}

@Composable
private fun NotificationScreen(viewModel: NotificationViewModel = hiltViewModel()) {
    val notificationState: NotificationState by
        viewModel.notificationState.collectAsStateWithLifecycle()

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
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher
    KuiScaffold(
        containerColor = KuiTheme.colorScheme.surface,
        contentColor = KuiTheme.colorScheme.onSurface,
        topBar = {
            KuiCenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor =
                    if (isSystemInDarkTheme()) {
                        KuiTheme.colorScheme.surfaceVariant.copy(alpha = if (hasScrolled) 1f else 0f)
                    } else {
                        KuiTheme.colorScheme.surface
                    },
                ),
                modifier = Modifier.shadow(appBarElevation),
                title = { KuiText(text = stringResource(Res.string.settings_notification_title)) },
                navigationIcon = {
                    KuiIconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        KuiIcon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(Res.string.settings_back_description))
                    }
                },
                actions = {},
            )
        },
    ) { padding ->
        NotificationContent(
            uiState = notificationState,
            listState = listState,
            paddingValues = padding,
        )
    }
}

@Composable
private fun NotificationContent(
    uiState: NotificationState,
    listState: LazyListState,
    paddingValues: PaddingValues,
) {
    when (uiState) {
        is NotificationState.Empty -> {
            EmptyView()
        }

        is NotificationState.Success -> {
            LazyColumn(
                modifier =
                Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = space12,
                    end = space12,
                ),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(space12),
            ) {
                if (uiState.fixedItems.isNotEmpty()) {
                    stickyHeader {
                        KuiIcon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(Res.string.settings_notification_fixed),
                        )
                    }
                    items(
                        items = uiState.fixedItems,
                        key = { it.title + it.date + it.desc },
                    ) { notification ->
                        NotificationListCard(
                            notificationDate = notification.date,
                            notificationTitle = notification.title,
                            notificationDesc = notification.desc,
                            modifier = Modifier.animateItem(
                                placementSpec = tween(
                                    durationMillis = 500,
                                ),
                            ),
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(space4),
                        )
                        KuiHorizontalDivider(
                            thickness = space2,
                        )
                        Spacer(
                            modifier = Modifier.height(space4),
                        )
                    }
                }
                items(
                    uiState.generalItems,
                    key = { it.title + it.date + it.desc },
                ) { notification ->
                    NotificationListCard(
                        notificationDate = notification.date,
                        notificationTitle = notification.title,
                        notificationDesc = notification.desc,
                        modifier = Modifier.animateItem(
                            placementSpec = tween(
                                durationMillis = 500,
                            ),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationListCard(
    notificationDate: String,
    notificationTitle: String,
    notificationDesc: String,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.padding(
            start = space16,
        ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(space16),
            horizontalArrangement = Arrangement.spacedBy(space8),
        ) {
            KuiText(
                text = notificationDate,
                style = KuiTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                KuiText(
                    text = notificationTitle,
                    style = KuiTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                KuiText(
                    text = notificationDesc,
                    style = KuiTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNotificationScreen() {
    NotificationScreen()
}

@Preview
@Composable
private fun PreviewNotificationListCard() {
    NotificationListCard(
        notificationDate = "2022.12.13",
        notificationTitle = "공지 제목",
        notificationDesc = "공지 설명",
    )
}
