@file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Share
import com.keelim.core.designsystem.component.KuiAssistChip
import androidx.compose.material3.AssistChipDefaults
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space32
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.core.designsystem.component.KuiBadge
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.resource.*
import com.keelim.model.DeepLink
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainTopSection(
    schemeList: List<String>,
    onSearch: (String, String, String) -> Unit,
    onRegister: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space8),
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        val (text, setText) = remember { mutableStateOf("") }
        val (title, setTitle) = remember { mutableStateOf("") }
        val (isError, setError) = remember { mutableStateOf(false) }
        val (category, setCategory) = remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            KuiFilledTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                isError = isError,
                onValueChange = setText,
                label = { KuiText("Deeplink URL") },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        KuiIcon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setText("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onSearch(text, title, category)
                        }
                    },
                ),
            )

            KuiFilledTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                isError = isError,
                onValueChange = setTitle,
                label = { KuiText("Title") },
                trailingIcon = {
                    if (title.isNotEmpty()) {
                        KuiIcon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setTitle("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )
        }

        RegisterSchemeSection(
            schemeList = schemeList,
            setError = setError,
            setText = setText,
            onRegister = onRegister,
            onDelete = onDelete,
        )
    }
}

@Composable
fun RegisterSchemeSection(
    schemeList: List<String>,
    setError: (Boolean) -> Unit,
    setText: (String) -> Unit,
    onRegister: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (scheme, setScheme) = remember { mutableStateOf("") }
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space8)
            .animateContentSize(
                animationSpec = tween(durationMillis = 300),
            ),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KuiFilledTextField(
                modifier = Modifier.weight(1f),
                value = scheme,
                onValueChange = setScheme,
                label = { KuiText("Scheme") },
                trailingIcon = {
                    if (scheme.isNotEmpty()) {
                        KuiIcon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setScheme("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (scheme.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onRegister(scheme)
                        }
                    },
                ),
            )
            Spacer(modifier = Modifier.width(space8))
            KuiIcon(
                imageVector = Icons.Default.Add,
                modifier = Modifier
                    .size(space32)
                    .clickable {
                        if (scheme.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onRegister(scheme)
                        }
                    },
                contentDescription = "Register",
            )
        }

        if (isExpanded) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                schemeList.forEach { scheme ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                            animationSpec = tween(300),
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                            animationSpec = tween(300),
                        ),
                    ) {
                        KuiAssistChip(
                            onClick = {
                                setError(false)
                                setText("$scheme://")
                            },
                            label = { KuiText("$scheme://") },
                            leadingIcon = {
                                KuiIcon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add $scheme",
                                    Modifier.size(AssistChipDefaults.IconSize),
                                )
                            },
                            trailingIcon = {
                                KuiIcon(
                                    Icons.Default.Close,
                                    contentDescription = "Delete $scheme",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable { onDelete(scheme) },
                                )
                            },
                        )
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                items(schemeList) { scheme ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                            animationSpec = tween(300),
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                            animationSpec = tween(300),
                        ),
                    ) {
                        KuiAssistChip(
                            onClick = {
                                setError(false)
                                setText("$scheme://")
                            },
                            label = { KuiText("$scheme://") },
                            leadingIcon = {
                                KuiIcon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add $scheme",
                                    Modifier.size(AssistChipDefaults.IconSize),
                                )
                            },
                            trailingIcon = {
                                KuiIcon(
                                    Icons.Default.Close,
                                    contentDescription = "Delete $scheme",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable { onDelete(scheme) },
                                )
                            },
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            KuiIcon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (isExpanded) "Close" else "Open",
                modifier = Modifier
                    .clickable { setIsExpanded(!isExpanded) },
            )
        }
    }
}

@Composable
fun DeepLinkSection(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    schemeList: List<String>,
    categories: List<String>,
    onSearch: (String, String, String) -> Unit,
    onRegister: (String) -> Unit,
    onDeleteScheme: (String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    onItemLongClick: (DeepLink) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSaastatus: () -> Unit,
    onNavigateOgTagPreview: () -> Unit,
    onNavigatePlayground: () -> Unit,
    onNavigateJsonFormatter: () -> Unit,
    onNavigateBase64Encoder: () -> Unit,
    onNavigateDeviceInfo: () -> Unit,
    onNavigateDeviceTestLab: () -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onShowNotification: (Int, String, String, String) -> Unit,
    onGenerateQrCode: (DeepLink) -> Unit,
    recordDeepLinkUsage: (DeepLink) -> Unit,
    onNavigateStats: () -> Unit,
    onNavigateUrlShortener: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        item {
            ArduconToolHubSection(
                onToolClick = { action ->
                    when (action) {
                        ArduconToolAction.Playground -> onNavigatePlayground()
                        ArduconToolAction.QrScanner -> onQrCodeClick()
                        ArduconToolAction.SchemeSearch -> onNavigateSearch()
                        ArduconToolAction.OgTagPreview -> onNavigateOgTagPreview()
                        ArduconToolAction.JsonFormatter -> onNavigateJsonFormatter()
                        ArduconToolAction.Base64 -> onNavigateBase64Encoder()
                            ArduconToolAction.UrlShortener -> onNavigateUrlShortener()
                            ArduconToolAction.DeviceInfo -> onNavigateDeviceInfo()
                            ArduconToolAction.DeviceTestLab -> onNavigateDeviceTestLab()
                            ArduconToolAction.Saastatus -> onNavigateSaastatus()
                            ArduconToolAction.Stats -> onNavigateStats()
                        }
                },
                modifier = Modifier.animateItem(
                    placementSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
        item {
            DeepLinkHistoryHeader(
                modifier = Modifier.animateItem(
                    placementSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
        item {
            MainTopSection(
                schemeList = schemeList,
                onSearch = onSearch,
                onRegister = onRegister,
                onDelete = onDeleteScheme,
            )
            KuiHorizontalDivider(
                color = KuiTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = space8),
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                items(
                    items = listOf("") + categories,
                    key = { it },
                ) { category ->
                    val isSelected = selectedCategory == category
                    KuiAssistChip(
                        onClick = { onCategorySelected(category) },
                        label = {
                            KuiText(
                                text = category.ifEmpty { stringResource(Res.string.arducon_tool_category_all) },
                                style = KuiTheme.typography.labelLarge,
                                color = if (isSelected) {
                                    KuiTheme.colorScheme.onPrimaryContainer
                                } else {
                                    KuiTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) KuiTheme.colorScheme.primaryContainer else KuiTheme.colorScheme.surfaceVariant,
                            labelColor = if (isSelected) KuiTheme.colorScheme.onPrimaryContainer else KuiTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
        if (favoriteItems.isEmpty() && generalItems.isEmpty()) {
            item {
                DeepLinkHistoryEmptyCard(
                    modifier = Modifier.animateItem(
                        placementSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing,
                        ),
                    ),
                )
            }
        }
        if (favoriteItems.isNotEmpty()) {
            stickyHeader {
                KuiText(
                    text = stringResource(Res.string.arducon_tool_history_favorite),
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = space8),
                )
            }
            items(
                items = favoriteItems,
                key = { it.timestamp },
            ) {
                val (isMoved, setMoved) = remember { mutableStateOf("") }
                val context = LocalContext.current
                if (isMoved.isNotEmpty()) {
                    LaunchedEffect(context, isMoved) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                isMoved.toUri(),
                            ),
                        )
                        setMoved("")
                    }
                }
                DeepLinkItem(
                    deepLink = it,
                    onPlay = { uri ->
                        recordDeepLinkUsage(it)
                        setMoved(uri)
                    },
                    onUpdate = onUpdate,
                    onDelete = onDelete,
                    onItemLongClick = onItemLongClick,
                    modifier = Modifier.animateItem(
                        placementSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing,
                        ),
                    ),
                    onCategoryClick = onCategorySelected,
                    onShowNotification = onShowNotification,
                    onGenerateQrCode = onGenerateQrCode,
                )
            }
        }
        if (generalItems.isNotEmpty()) {
            stickyHeader {
                KuiText(
                    text = stringResource(Res.string.arducon_tool_history_general),
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = space8),
                )
            }
            items(
                items = generalItems,
                key = { it.timestamp },
            ) {
                val (isMoved, setMoved) = remember { mutableStateOf("") }
                val context = LocalContext.current
                if (isMoved.isNotEmpty()) {
                    LaunchedEffect(context, isMoved) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                isMoved.toUri(),
                            ),
                        )
                        setMoved("")
                    }
                }
                DeepLinkItem(
                    deepLink = it,
                    onPlay = { uri ->
                        setMoved(uri)
                    },
                    onUpdate = onUpdate,
                    onDelete = onDelete,
                    onItemLongClick = onItemLongClick,
                    modifier = Modifier.animateItem(
                        placementSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing,
                        ),
                    ),
                    onCategoryClick = onCategorySelected,
                    onShowNotification = onShowNotification,
                    onGenerateQrCode = onGenerateQrCode,
                )
            }
        }
    }
}

@Composable
private fun ArduconToolHubSection(
    onToolClick: (ArduconToolAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = KuiTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(space16),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(space4)) {
                KuiText(
                    text = stringResource(Res.string.arducon_tool_hub_title),
                    style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = KuiTheme.colorScheme.primary,
                )
                KuiText(
                    text = stringResource(Res.string.arducon_tool_hub_subtitle),
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }

            arduconToolGroups.forEach { group ->
                ArduconToolGroupSection(
                    group = group,
                    onToolClick = onToolClick,
                )
            }
        }
    }
}

@Composable
private fun ArduconToolGroupSection(
    group: ArduconToolGroup,
    onToolClick: (ArduconToolAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        KuiText(
            text = stringResource(group.title),
            style = KuiTheme.typography.titleMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
        Column(verticalArrangement = Arrangement.spacedBy(space8)) {
            group.items.forEach { item ->
                ArduconToolCard(
                    item = item,
                    onClick = { onToolClick(item.action) },
                )
            }
        }
    }
}

@Composable
private fun ArduconToolCard(
    item: ArduconToolItem,
    onClick: () -> Unit,
) {
    KuiCard(padded = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = KuiTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space12),
        ) {
            KuiIcon(
                imageVector = item.icon,
                contentDescription = null,
                tint = KuiTheme.colorScheme.primary,
                modifier = Modifier.size(space24),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(space4),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    KuiText(
                        text = stringResource(item.title),
                        style = KuiTheme.typography.titleSmall,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                    KuiBadge(text = stringResource(item.badge))
                }
                KuiText(
                    text = stringResource(item.description),
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
                )
            }
        }
    }
}

@Composable
private fun DeepLinkHistoryHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = space8),
        verticalArrangement = Arrangement.spacedBy(space4),
    ) {
        KuiText(
            text = stringResource(Res.string.arducon_tool_deeplink_history_title),
            style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = KuiTheme.colorScheme.primary,
        )
        KuiText(
            text = stringResource(Res.string.arducon_tool_deeplink_history_desc),
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DeepLinkHistoryEmptyCard(
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = KuiTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(space16),
            verticalArrangement = Arrangement.spacedBy(space4),
        ) {
            KuiText(
                text = stringResource(Res.string.arducon_tool_history_empty_title),
                style = KuiTheme.typography.titleMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            KuiText(
                text = stringResource(Res.string.arducon_tool_history_empty_desc),
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
            )
        }
    }
}

@Composable
private fun DeepLinkItem(
    deepLink: DeepLink,
    onPlay: (String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    onItemLongClick: (DeepLink) -> Unit,
    onCategoryClick: (String) -> Unit,
    onShowNotification: (Int, String, String, String) -> Unit,
    onGenerateQrCode: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onItemLongClick(deepLink)
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = KuiTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(space8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (deepLink.imageUrl.isEmpty()) {
                KuiIcon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier
                        .size(space64)
                        .padding(space8),
                    contentDescription = "",
                    tint = KuiTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                AsyncImage(
                    model = deepLink.imageUrl,
                    modifier = Modifier
                        .size(space64)
                        .clip(KuiTheme.shapes.medium),
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = space8),
                verticalArrangement = Arrangement.spacedBy(space4),
            ) {
                if (deepLink.title.isNotEmpty()) {
                    KuiText(
                        text = deepLink.title,
                        style = KuiTheme.typography.titleMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                }
                KuiText(
                    text = deepLink.url,
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                )
                if (deepLink.category.isNotEmpty()) {
                    KuiAssistChip(
                        onClick = { onCategoryClick(deepLink.category) },
                        label = {
                            KuiText(
                                text = deepLink.category,
                                style = KuiTheme.typography.bodySmall,
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = KuiTheme.colorScheme.secondaryContainer,
                            labelColor = KuiTheme.colorScheme.onSecondaryContainer,
                        ),
                        modifier = Modifier.padding(top = space4),
                    )
                }
            }

            AnimatedContent(
                targetState = deepLink.isBookMarked,
                label = "bookmark",
            ) { targetState ->
                KuiIcon(
                    imageVector = if (targetState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "bookmark",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onUpdate(deepLink) },
                    tint = if (targetState) KuiTheme.colorScheme.primary else KuiTheme.colorScheme.onSurfaceVariant,
                )
            }

            FlowRow(
                modifier = Modifier.padding(start = space8),
                horizontalArrangement = Arrangement.spacedBy(space4),
                maxItemsInEachRow = 2,
            ) {
                KuiIcon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "show notification",
                    modifier = Modifier
                        .size(space32)
                        .clickable {
                            onShowNotification(
                                deepLink.hashCode(),
                                deepLink.title.ifEmpty { "Deep Link Notification" },
                                "Click to open: ${deepLink.url}",
                                deepLink.url,
                            )
                        },
                    tint = KuiTheme.colorScheme.primary,
                )
                KuiIcon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onPlay(deepLink.url) },
                    tint = KuiTheme.colorScheme.primary,
                )
                KuiIcon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "QR 코드 생성",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onGenerateQrCode(deepLink) },
                    tint = KuiTheme.colorScheme.primary,
                )
                KuiIcon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onDelete(deepLink) },
                    tint = KuiTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainTopSection() {
    MainTopSection(
        schemeList = listOf("https", "http"),
        onSearch = { _, _, _ -> },
        onRegister = {},
        onDelete = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeepLinkSection() {
    DeepLinkSection(
        favoriteItems = listOf(),
        generalItems = listOf(
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232121223L,
                title = "naver",
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 23232L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232123L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232323L,
            ),
        ),
        onUpdate = {},
        onDelete = {},
        schemeList = emptyList(),
        onSearch = { _, _, _ -> },
        onRegister = {},
        onDeleteScheme = {},
        onItemLongClick = {},
        onQrCodeClick = {},
        onNavigateSearch = {},
        onNavigateSaastatus = {},
        onNavigateOgTagPreview = {},
        onNavigatePlayground = {},
        onNavigateJsonFormatter = {},
        onNavigateBase64Encoder = {},
        onNavigateDeviceInfo = {},
        categories = emptyList(),
        selectedCategory = "",
        onCategorySelected = {},
        onShowNotification = { _, _, _, _ -> },
        onGenerateQrCode = {},
        recordDeepLinkUsage = { },
        onNavigateStats = {},
        onNavigateUrlShortener = {},
        onNavigateDeviceTestLab = {},
    )
}
