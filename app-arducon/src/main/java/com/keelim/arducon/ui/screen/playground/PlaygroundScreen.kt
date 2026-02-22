package com.keelim.arducon.ui.screen.playground

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.common.extensions.saveQrBitmapToGallery
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.*
import com.keelim.model.linkinspector.HttpResult
import com.keelim.model.linkinspector.OgResult
import com.keelim.model.linkinspector.ResolvedApp
import java.io.File
import java.io.FileOutputStream
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaygroundRoute(
    onNavigateBack: () -> Unit,
    viewModel: PlaygroundViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    val qrState by viewModel.qrDialogState.collectAsStateWithLifecycle()
    val playgroundTitle = stringResource(Res.string.playground_title)
    PlaygroundScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onUrlChange = viewModel::updateUrl,
        onParamKeyChange = viewModel::updateParamKey,
        onParamValueChange = viewModel::updateParamValue,
        onRunValidation = viewModel::validate,
        onShare = { uiState ->
            val text = viewModel.buildShareText(uiState)
            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, text)
            }
            val chooser = android.content.Intent.createChooser(shareIntent, playgroundTitle)
            context.startActivity(chooser)
            text
        },
        onGenerateQr = viewModel::generateQrCode,
        onDismissQr = viewModel::hideQrDialog,
        qrDialogState = qrState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(
    state: PlaygroundUiState,
    onNavigateBack: () -> Unit,
    onUrlChange: (String) -> Unit,
    onParamKeyChange: (String) -> Unit,
    onParamValueChange: (String) -> Unit,
    onRunValidation: () -> Unit,
    onShare: (PlaygroundUiState) -> String,
    onGenerateQr: () -> Unit,
    onDismissQr: () -> Unit,
    qrDialogState: PlaygroundViewModel.QrDialogState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.playground_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = space16),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            OutlinedTextField(
                value = state.url,
                onValueChange = onUrlChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.label_url)) },
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(space8)) {
                OutlinedTextField(
                    value = state.paramKey,
                    onValueChange = onParamKeyChange,
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(Res.string.label_param_key)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.paramValue,
                    onValueChange = onParamValueChange,
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(Res.string.label_param_value)) },
                    singleLine = true,
                )
            }

            Text(text = "Preview: ${'$'}{state.preview}")

            Button(onClick = onRunValidation, enabled = state.url.isNotBlank()) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Text(text = stringResource(Res.string.action_run_validation), modifier = Modifier.padding(start = space8))
            }

            if (state.isLoading) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
            }

            if (state.error != null) {
                ErrorCard(message = state.error)
            }

            if (!state.isLoading && state.error == null) {
                if (state.resolvedApps.isNotEmpty()) {
                    ResolvedAppsCard(state.resolvedApps)
                }
                state.http?.let { HttpResultCard(it) }
                state.og?.let { OgResultCard(it) }
                if (state.resultText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        val text = onShare(state)
                        // sharing will be handled in Route if we pass a callback with context, but for now we just build text
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }) {
                        Text(stringResource(Res.string.action_share_report))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onGenerateQr, enabled = state.preview.isNotBlank() || state.url.isNotBlank()) {
                        Text(stringResource(Res.string.action_generate_qr))
                    }
                }
            }
        }
    }

    QrDialogSection(qrDialogState = qrDialogState, onDismiss = onDismissQr)
}

@Composable
private fun QrDialogSection(
    qrDialogState: PlaygroundViewModel.QrDialogState,
    onDismiss: () -> Unit,
) {
    when (qrDialogState) {
        is PlaygroundViewModel.QrDialogState.Hidden -> Unit
        is PlaygroundViewModel.QrDialogState.Loading -> {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = onDismiss,
                text = { androidx.compose.material3.CircularProgressIndicator() },
                confirmButton = {},
                dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.dialog_close)) } },
            )
        }
        is PlaygroundViewModel.QrDialogState.Success -> {
            val context = LocalContext.current
            val playgroundTitle = stringResource(Res.string.playground_title)
            androidx.compose.material3.AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    Image(
                        bitmap = qrDialogState.bitmap.asImageBitmap(),
                        contentDescription = stringResource(Res.string.qr_content_description),
                    )
                },
                confirmButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(space8)) {
                        Button(onClick = { context.saveQrBitmapToGallery(qrDialogState.bitmap) }) {
                            Text(stringResource(Res.string.qr_save_image))
                        }
                        Button(onClick = {
                            val cacheDir = File(context.cacheDir, "images").apply { mkdirs() }
                            val outFile = File(cacheDir, "qr_share.png")
                            FileOutputStream(outFile).use { stream ->
                                qrDialogState.bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
                            }
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", outFile)
                            val share = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(android.content.Intent.createChooser(share, playgroundTitle))
                        }) {
                            Text(stringResource(Res.string.qr_share_image))
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text(stringResource(Res.string.dialog_close)) }
                },
            )
        }
        is PlaygroundViewModel.QrDialogState.Error -> {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(stringResource(Res.string.dialog_error)) },
                text = { Text(qrDialogState.message) },
                confirmButton = {},
                dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.dialog_close)) } },
            )
        }
    }
}

@Composable
private fun ResolvedAppsCard(apps: List<ResolvedApp>) {
    var expanded by remember { androidx.compose.runtime.mutableStateOf(true) }
    val clipboard = LocalClipboardManager.current
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(space16), verticalArrangement = Arrangement.spacedBy(space8)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(Res.string.label_resolved_apps), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = {
                        val text = apps.joinToString("\n") { "${'$'}{it.label} (${ '$'}{it.packageName})" }
                        clipboard.setText(AnnotatedString(text))
                    }) { Icon(Icons.Default.Check, contentDescription = null) }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.PlayArrow else Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            }
            if (expanded) {
                apps.forEach { _ ->
                    Text(
                        text = "- ${'$'}{it.label} (${'$'}{it.packageName})",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun HttpResultCard(http: HttpResult) {
    var expanded by remember { androidx.compose.runtime.mutableStateOf(true) }
    val clipboard = LocalClipboardManager.current
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(space16), verticalArrangement = Arrangement.spacedBy(space8)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(Res.string.label_http_result), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = {
                        val headers = http.headers.entries.joinToString("\n") { (k, v) -> "$k: ${'$'}{v.joinToString()}" }
                        val text = "${'$'}{http.statusCode} ${'$'}{http.finalUrl}\n${'$'}headers"
                        clipboard.setText(AnnotatedString(text))
                    }) { Icon(Icons.Default.Check, contentDescription = null) }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.PlayArrow else Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            }
            Text(text = stringResource(Res.string.label_status_code, http.statusCode))
            Text(text = stringResource(Res.string.label_final_url, http.finalUrl))
            if (expanded) {
                if (http.redirects.isNotEmpty()) {
                    Text(stringResource(Res.string.label_redirects))
                    http.redirects.forEach { Text("- ${'$'}it") }
                }
                if (http.headers.isNotEmpty()) {
                    Text(stringResource(Res.string.label_headers))
                    http.headers.forEach { (k, v) -> Text("${'$'}k: ${'$'}{v.joinToString()}") }
                }
            }
        }
    }
}

@Composable
private fun OgResultCard(og: OgResult) {
    var expanded by remember { androidx.compose.runtime.mutableStateOf(true) }
    val clipboard = LocalClipboardManager.current
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(space16), verticalArrangement = Arrangement.spacedBy(space8)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(Res.string.label_og_result), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = {
                        val text = "title=${'$'}{og.title}\ndesc=${'$'}{og.description}\nimage=${'$'}{og.image}"
                        clipboard.setText(AnnotatedString(text))
                    }) { Icon(Icons.Default.Check, contentDescription = null) }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.PlayArrow else Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            }
            Text(text = stringResource(Res.string.label_og_title, og.title ?: "-"))
            if (expanded) {
                Text(text = stringResource(Res.string.label_og_desc, og.description ?: "-"))
                Text(text = stringResource(Res.string.label_og_image, og.image ?: "-"))
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Column(modifier = Modifier.padding(space16)) {
            Text(text = message, color = MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}
