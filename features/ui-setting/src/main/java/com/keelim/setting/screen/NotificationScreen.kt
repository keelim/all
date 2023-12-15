@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.setting.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView

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
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor =
                    if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (hasScrolled) 1f else 0f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                ),
                modifier = Modifier.shadow(appBarElevation),
                title = { Text(text = "Notifications") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Go back")
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
                    start = 12.dp,
                    end = 12.dp,
                ),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.items) { notification ->
                    NotificationListCard(
                        notificationDate = notification.date,
                        notificationTitle = notification.title,
                        notificationDesc = notification.desc,
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
) {
    val context = LocalContext.current
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = notificationDate,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = notificationTitle,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = notificationDesc,
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNotificationScreen() {
    NotificationScreen()
}

@Preview
@Composable
fun PreviewNotificationListCard() {
    NotificationListCard(
        notificationDate = "2022.12.13",
        notificationTitle = "공지 제목",
        notificationDesc = "공지 설명",
    )
}
