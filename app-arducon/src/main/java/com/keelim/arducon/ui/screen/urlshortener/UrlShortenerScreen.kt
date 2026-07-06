package com.keelim.arducon.ui.screen.urlshortener

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiDropdownMenu
import com.keelim.core.designsystem.component.KuiDropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.common.extensions.toUiNumber
import com.keelim.commonAndroid.extensions.toUiDate
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlShortenerScreen(
    onNavigateBack: () -> Unit,
    viewModel: UrlShortenerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shortenedUrls by viewModel.shortenedUrls.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val copiedToClipboardMessage = stringResource(Res.string.common_copied_to_clipboard)
    val originalUrlCopiedMessage = stringResource(Res.string.arducon_url_shortener_original_url_copied)
    val shareAction = stringResource(Res.string.common_action_share)

    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    KuiText(
                        text = stringResource(Res.string.arducon_url_shortener_title),
                        style = KuiTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = KuiTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    KuiIconButton(onClick = onNavigateBack) {
                        KuiIcon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(Res.string.arducon_back_description))
                    }
                },
            )
        },
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = space16),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            item {
                KuiOutlinedTextField(
                    value = uiState.inputUrl,
                    onValueChange = viewModel::updateInputUrl,
                    modifier = Modifier.fillMaxWidth(),
                    label = { KuiText(stringResource(Res.string.label_url)) },
                    placeholder = { KuiText(stringResource(Res.string.arducon_url_shortener_url_placeholder)) },
                    singleLine = true,
                )
            }

            item {
                KuiOutlinedTextField(
                    value = uiState.inputTitle,
                    onValueChange = viewModel::updateInputTitle,
                    modifier = Modifier.fillMaxWidth(),
                    label = { KuiText(stringResource(Res.string.arducon_url_shortener_title_optional)) },
                    placeholder = { KuiText(stringResource(Res.string.arducon_url_shortener_title_placeholder)) },
                    singleLine = true,
                )
            }

            item {
                ExpirationSelector(
                    selectedDays = uiState.expirationDays,
                    onDaysSelected = viewModel::updateExpirationDays,
                )
            }

            item {
                KuiButton(
                    onClick = viewModel::generateShortUrl,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                ) {
                    KuiText(stringResource(Res.string.arducon_url_shortener_generate))
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    KuiCard(padded = false,
                        colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        KuiText(
                            text = uiState.errorMessage!!,
                            color = KuiTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(space16),
                            style = KuiTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            if (uiState.generatedShortCode.isNotEmpty()) {
                item {
                    KuiCard(padded = false,
                        colors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(space16)) {
                            KuiText(
                                text = stringResource(Res.string.arducon_url_shortener_generated_title),
                                style = KuiTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = KuiTheme.colorScheme.onPrimaryContainer,
                            )
                            Spacer(modifier = Modifier.height(space8))
                            KuiText(
                                text = stringResource(Res.string.arducon_url_shortener_short_code, uiState.generatedShortCode),
                                style = KuiTheme.typography.bodyLarge,
                                color = KuiTheme.colorScheme.onPrimaryContainer,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                KuiTextButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(uiState.generatedShortCode))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(copiedToClipboardMessage)
                                    }
                                    viewModel.clearGeneratedCode()
                                }) {
                                    KuiText(stringResource(Res.string.common_action_copy))
                                }
                            }
                        }
                    }
                }
            }

            if (shortenedUrls.isNotEmpty()) {
                item {
                    KuiText(
                        text = stringResource(Res.string.arducon_url_shortener_saved_links, shortenedUrls.size.toUiNumber()),
                        style = KuiTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = KuiTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = space8),
                    )
                }

                items(
                    items = shortenedUrls,
                    key = { it.id },
                ) { item ->
                    ShortenedUrlCard(
                        item = item,
                        onItemClick = {
                            viewModel.recordClick(item)
                            Intent(Intent.ACTION_VIEW, item.originalUrl.toUri())
                                .let { context.startActivity(it) }
                        },
                        onCopyClick = {
                            clipboardManager.setText(AnnotatedString(item.originalUrl))
                            scope.launch {
                                snackbarHostState.showSnackbar(originalUrlCopiedMessage)
                            }
                        },
                        onShareClick = {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, item.originalUrl)
                            }.let { Intent.createChooser(it, shareAction) }
                                .let { context.startActivity(it) }
                        },
                        onDeleteClick = { viewModel.deleteItem(item) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpirationSelector(
    selectedDays: Int,
    onDaysSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        0 to stringResource(Res.string.arducon_url_shortener_no_expiration),
        1 to stringResource(Res.string.arducon_url_shortener_expiration_one_day),
        7 to stringResource(Res.string.arducon_url_shortener_expiration_one_week),
        30 to stringResource(Res.string.arducon_url_shortener_expiration_one_month),
        90 to stringResource(Res.string.arducon_url_shortener_expiration_three_months),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuiIcon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = KuiTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(space8))
        KuiText(
            text = stringResource(Res.string.arducon_url_shortener_expiration_label),
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(space8))
        KuiTextButton(onClick = { expanded = true }) {
            KuiText(options.find { it.first == selectedDays }?.second ?: stringResource(Res.string.arducon_url_shortener_no_expiration))
        }
        KuiDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { (days, label) ->
                KuiDropdownMenuItem(
                    text = { KuiText(label) },
                    onClick = {
                        onDaysSelected(days)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ShortenedUrlCard(
    item: ShortenedUrlEntity,
    onItemClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(space16)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    KuiText(
                        text = item.title.ifEmpty { item.shortCode },
                        style = KuiTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = KuiTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(space4))
                    KuiText(
                        text = item.originalUrl,
                        style = KuiTheme.typography.bodySmall,
                        color = KuiTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row {
                    KuiIconButton(onClick = onShareClick) {
                        KuiIcon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.common_action_share),
                            tint = KuiTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    KuiIconButton(onClick = onDeleteClick) {
                        KuiIcon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Res.string.common_action_delete),
                            tint = KuiTheme.colorScheme.error,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(space8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                KuiText(
                    text = stringResource(Res.string.arducon_url_shortener_click_count, item.clickCount.toUiNumber()),
                    style = KuiTheme.typography.labelSmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
                KuiText(
                    text = formatDate(item.createdAt),
                    style = KuiTheme.typography.labelSmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }

            AnimatedVisibility(visible = item.expiresAt > 0) {
                KuiText(
                    text = stringResource(Res.string.arducon_url_shortener_expiration_at, formatDate(item.expiresAt)),
                    style = KuiTheme.typography.labelSmall,
                    color = KuiTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = space4),
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return try {
        Instant.fromEpochMilliseconds(timestamp).toUiDate()
    } catch (e: Exception) {
        ""
    }
}
