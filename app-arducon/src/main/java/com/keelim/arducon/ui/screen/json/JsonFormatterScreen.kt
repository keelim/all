package com.keelim.arducon.ui.screen.json

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JsonFormatterScreen(
    onNavigateBack: () -> Unit,
    viewModel: JsonFormatterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val copiedToClipboardMessage = stringResource(Res.string.common_copied_to_clipboard)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.arducon_json_formatter_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = space16)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            OutlinedTextField(
                value = uiState.inputJson,
                onValueChange = viewModel::updateInputJson,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                label = { Text(stringResource(Res.string.arducon_json_formatter_raw_json)) },
                placeholder = { Text(stringResource(Res.string.arducon_json_formatter_input_placeholder)) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                Button(
                    onClick = viewModel::formatJson,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(Res.string.arducon_json_formatter_format_validate))
                }
                Button(
                    onClick = viewModel::clear,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    Spacer(modifier = Modifier.padding(start = space8))
                    Text(stringResource(Res.string.common_action_clear))
                }
            }

            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.common_error_with_message, uiState.errorMessage ?: ""),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(space16),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            if (uiState.formattedJson.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(space16)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(Res.string.arducon_json_formatter_output_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(uiState.formattedJson))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(copiedToClipboardMessage)
                                    }
                                },
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = stringResource(Res.string.common_action_copy))
                            }
                        }
                        Spacer(modifier = Modifier.height(space8))
                        Text(
                            text = uiState.formattedJson,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                        )
                    }
                }
            }
        }
    }
}
