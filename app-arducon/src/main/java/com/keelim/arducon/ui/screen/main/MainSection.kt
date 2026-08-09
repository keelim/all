@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiEmptyState
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.core.designsystem.component.KuiBadge
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.resource.Res
import com.keelim.core.resource.arducon_action_favorite_add
import com.keelim.core.resource.arducon_action_favorite_remove
import com.keelim.core.resource.arducon_action_generate_qr
import com.keelim.core.resource.arducon_action_open_deeplink
import com.keelim.core.resource.arducon_action_show_notification
import com.keelim.core.resource.arducon_main_deeplink_url_label
import com.keelim.core.resource.arducon_main_title_label
import com.keelim.core.resource.arducon_scheme_collapse
import com.keelim.core.resource.arducon_scheme_expand
import com.keelim.core.resource.arducon_scheme_label
import com.keelim.core.resource.arducon_scheme_register
import com.keelim.core.resource.arducon_tool_category_all
import com.keelim.core.resource.arducon_tool_deeplink_history_desc
import com.keelim.core.resource.arducon_tool_deeplink_history_title
import com.keelim.core.resource.arducon_tool_history_empty_desc
import com.keelim.core.resource.arducon_tool_history_empty_title
import com.keelim.core.resource.arducon_tool_history_favorite
import com.keelim.core.resource.arducon_tool_history_general
import com.keelim.core.resource.arducon_tool_hub_subtitle
import com.keelim.core.resource.arducon_tool_hub_title
import com.keelim.core.resource.common_action_clear
import com.keelim.core.resource.common_action_delete
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
                label = {
                    KuiText(
                        text = stringResource(Res.string.arducon_main_deeplink_url_label),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        KuiIconButton(onClick = { setText("") }) {
                            KuiIcon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.common_action_clear),
                            )
                        }
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
                label = {
                    KuiText(
                        text = stringResource(Res.string.arducon_main_title_label),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    if (title.isNotEmpty()) {
                        KuiIconButton(onClick = { setTitle("") }) {
                            KuiIcon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.common_action_clear),
                            )
                        }
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
    val (selectedScheme, setSelectedScheme) = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space8),
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
                label = {
                    KuiText(
                        text = stringResource(Res.string.arducon_scheme_label),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    if (scheme.isNotEmpty()) {
                        KuiIconButton(onClick = { setScheme("") }) {
                            KuiIcon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.common_action_clear),
                            )
                        }
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
            KuiIconButton(
                onClick = {
                    if (scheme.isEmpty()) {
                        setError(true)
                    } else {
                        setError(false)
                        onRegister(scheme)
                    }
                },
            ) {
                KuiIcon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.arducon_scheme_register),
                )
            }
        }

        if (isExpanded) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                schemeList.forEach { scheme ->
                    key(scheme) {
                        RegisteredSchemeChip(
                            scheme = scheme,
                            isSelected = selectedScheme == scheme,
                            onSelect = {
                                setSelectedScheme(scheme)
                                setError(false)
                                setText("$scheme://")
                            },
                            onDelete = {
                                if (selectedScheme == scheme) setSelectedScheme(null)
                                onDelete(scheme)
                            },
                        )
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                items(schemeList, key = { it }) { scheme ->
                    RegisteredSchemeChip(
                        scheme = scheme,
                        isSelected = selectedScheme == scheme,
                        onSelect = {
                            setSelectedScheme(scheme)
                            setError(false)
                            setText("$scheme://")
                        },
                        onDelete = {
                            if (selectedScheme == scheme) setSelectedScheme(null)
                            onDelete(scheme)
                        },
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            KuiIconButton(onClick = { setIsExpanded(!isExpanded) }) {
                KuiIcon(
                    imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = stringResource(
                        if (isExpanded) {
                            Res.string.arducon_scheme_collapse
                        } else {
                            Res.string.arducon_scheme_expand
                        },
                    ),
                )
            }
        }
    }
}

@Composable
private fun RegisteredSchemeChip(
    scheme: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    val motionScheme = KuiTheme.motionScheme
    val contentColor = if (isSelected) {
        KuiTheme.colorScheme.onPrimaryContainer
    } else {
        KuiTheme.colorScheme.onSurfaceVariant
    }

    KuiAssistChip(
        onClick = onSelect,
        modifier = Modifier.semantics { selected = isSelected },
        label = {
            KuiText(
                text = "$scheme://",
                style = KuiTheme.typography.labelLarge,
                color = contentColor,
            )
        },
        leadingIcon = {
            AnimatedContent(
                targetState = isSelected,
                transitionSpec = {
                    fadeIn(motionScheme.fastEffectsSpec()) togetherWith
                        fadeOut(motionScheme.fastEffectsSpec())
                },
                label = "scheme-selected",
            ) { selected ->
                KuiIcon(
                    imageVector = if (selected) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                )
            }
        },
        trailingIcon = {
            KuiIconButton(
                onClick = onDelete,
                modifier = Modifier.size(KuiTheme.spacing.componentLg),
            ) {
                KuiIcon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.common_action_delete),
                    tint = KuiTheme.colorScheme.error,
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) {
                KuiTheme.colorScheme.primaryContainer
            } else {
                KuiTheme.colorScheme.surfaceVariant
            },
            labelColor = contentColor,
        ),
    )
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
    val placementSpec = KuiTheme.motionScheme.fastSpatialSpec<IntOffset>()

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        item(key = "tool-hub") {
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
                    placementSpec = placementSpec,
                ),
            )
        }
        item(key = "history-header") {
            DeepLinkHistoryHeader(
                modifier = Modifier.animateItem(
                    placementSpec = placementSpec,
                ),
            )
        }
        item(key = "history-form") {
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
        item(key = "category-filter") {
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
                        modifier = Modifier.semantics { selected = isSelected },
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
            item(key = "history-empty") {
                DeepLinkHistoryEmptyCard(
                    modifier = Modifier.animateItem(
                        placementSpec = placementSpec,
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
                        placementSpec = placementSpec,
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
                        placementSpec = placementSpec,
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space8),
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(space4)) {
            KuiText(
                text = stringResource(Res.string.arducon_tool_hub_title),
                style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = KuiTheme.colorScheme.onSurface,
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
        KuiCard(
            padded = false,
            colors = CardDefaults.cardColors(
                containerColor = KuiTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column {
                group.items.forEachIndexed { index, item ->
                    if (index > 0) {
                        KuiHorizontalDivider(color = KuiTheme.colorScheme.outlineVariant)
                    }
                    ArduconToolCard(
                        item = item,
                        onClick = { onToolClick(item.action) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ArduconToolCard(
    item: ArduconToolItem,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = space64)
            .clickable(role = Role.Button, onClick = onClick)
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
            KuiText(
                text = stringResource(item.title),
                style = KuiTheme.typography.titleSmall,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            KuiText(
                text = stringResource(item.description),
                style = KuiTheme.typography.bodySmall,
                color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
            )
        }
        KuiBadge(text = stringResource(item.badge))
        KuiIcon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = KuiTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(space24),
        )
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
    KuiEmptyState(
        title = stringResource(Res.string.arducon_tool_history_empty_title),
        description = stringResource(Res.string.arducon_tool_history_empty_desc),
        modifier = modifier.fillMaxWidth(),
    )
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
    val motionScheme = KuiTheme.motionScheme

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
                    contentDescription = null,
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
                                color = KuiTheme.colorScheme.onSecondaryContainer,
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

            KuiIconButton(onClick = { onUpdate(deepLink) }) {
                AnimatedContent(
                    targetState = deepLink.isBookMarked,
                    transitionSpec = {
                        fadeIn(motionScheme.defaultEffectsSpec()) togetherWith
                            fadeOut(motionScheme.defaultEffectsSpec())
                    },
                    label = "bookmark",
                ) { targetState ->
                    KuiIcon(
                        imageVector = if (targetState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(
                            if (targetState) {
                                Res.string.arducon_action_favorite_remove
                            } else {
                                Res.string.arducon_action_favorite_add
                            },
                        ),
                        tint = if (targetState) {
                            KuiTheme.colorScheme.primary
                        } else {
                            KuiTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }

            FlowRow(
                modifier = Modifier.padding(start = space8),
                horizontalArrangement = Arrangement.spacedBy(space4),
                maxItemsInEachRow = 2,
            ) {
                KuiIconButton(
                    onClick = {
                        onShowNotification(
                            deepLink.hashCode(),
                            deepLink.title.ifEmpty { "Deep Link Notification" },
                            "Click to open: ${deepLink.url}",
                            deepLink.url,
                        )
                    },
                ) {
                    KuiIcon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(Res.string.arducon_action_show_notification),
                        tint = KuiTheme.colorScheme.primary,
                    )
                }
                KuiIconButton(onClick = { onPlay(deepLink.url) }) {
                    KuiIcon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(Res.string.arducon_action_open_deeplink),
                        tint = KuiTheme.colorScheme.primary,
                    )
                }
                KuiIconButton(onClick = { onGenerateQrCode(deepLink) }) {
                    KuiIcon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(Res.string.arducon_action_generate_qr),
                        tint = KuiTheme.colorScheme.primary,
                    )
                }
                KuiIconButton(onClick = { onDelete(deepLink) }) {
                    KuiIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(Res.string.common_action_delete),
                        tint = KuiTheme.colorScheme.error,
                    )
                }
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
