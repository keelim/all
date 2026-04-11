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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.arducon_url_shortener_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(Res.string.arducon_back_description))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = space16),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            item {
                OutlinedTextField(
                    value = uiState.inputUrl,
                    onValueChange = viewModel::updateInputUrl,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.label_url)) },
                    placeholder = { Text(stringResource(Res.string.arducon_url_shortener_url_placeholder)) },
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.inputTitle,
                    onValueChange = viewModel::updateInputTitle,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.arducon_url_shortener_title_optional)) },
                    placeholder = { Text(stringResource(Res.string.arducon_url_shortener_title_placeholder)) },
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
                Button(
                    onClick = viewModel::generateShortUrl,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                ) {
                    Text(stringResource(Res.string.arducon_url_shortener_generate))
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(space16),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            if (uiState.generatedShortCode.isNotEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(space16)) {
                            Text(
                                text = stringResource(Res.string.arducon_url_shortener_generated_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Spacer(modifier = Modifier.height(space8))
                            Text(
                                text = stringResource(Res.string.arducon_url_shortener_short_code, uiState.generatedShortCode),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                TextButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(uiState.generatedShortCode))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(copiedToClipboardMessage)
                                    }
                                    viewModel.clearGeneratedCode()
                                }) {
                                    Text(stringResource(Res.string.common_action_copy))
                                }
                            }
                        }
                    }
                }
            }

            if (shortenedUrls.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(Res.string.arducon_url_shortener_saved_links, shortenedUrls.size.toUiNumber()),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
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
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(space8))
        Text(
            text = stringResource(Res.string.arducon_url_shortener_expiration_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(space8))
        TextButton(onClick = { expanded = true }) {
            Text(options.find { it.first == selectedDays }?.second ?: stringResource(Res.string.arducon_url_shortener_no_expiration))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { (days, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
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
    Card(
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
                    Text(
                        text = item.title.ifEmpty { item.shortCode },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(space4))
                    Text(
                        text = item.originalUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row {
                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.common_action_share),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Res.string.common_action_delete),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(space8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.arducon_url_shortener_click_count, item.clickCount.toUiNumber()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatDate(item.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            AnimatedVisibility(visible = item.expiresAt > 0) {
                Text(
                    text = stringResource(Res.string.arducon_url_shortener_expiration_at, formatDate(item.expiresAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
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
