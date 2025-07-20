package com.keelim.arducon.ui.screen.deeplink

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8

@Composable
fun CreateDeepLinkRoute(
    scheme: String,
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: CreateDeepLinkViewModel = hiltViewModel(),
) {
    val schemeState by viewModel.scheme.collectAsStateWithLifecycle()
    val urlState by viewModel.url.collectAsStateWithLifecycle()
    val titleState by viewModel.title.collectAsStateWithLifecycle()
    val categoryState by viewModel.category.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    // 스킴 설정
    LaunchedEffect(scheme) {
        viewModel.setScheme(scheme)
    }

    // 성공 상태 처리
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onShowMessage("딥링크가 성공적으로 생성되었습니다!")
            viewModel.resetSuccess()
            onNavigateBack()
        }
    }

    CreateDeepLinkScreen(
        scheme = schemeState,
        url = urlState,
        title = titleState,
        category = categoryState,
        categories = categories,
        isLoading = isLoading,
        onUrlChange = viewModel::updateUrl,
        onTitleChange = viewModel::updateTitle,
        onCategoryChange = viewModel::updateCategory,
        onCreateDeepLink = viewModel::createDeepLink,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeepLinkScreen(
    scheme: String,
    url: String,
    title: String,
    category: String,
    categories: List<String>,
    isLoading: Boolean,
    onUrlChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onCreateDeepLink: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isCategoryExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "딥링크 생성",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = space16)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space24),
        ) {
            // 스킴 정보 카드
            SchemeInfoCard(scheme = scheme)

            // URL 입력
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                OutlinedTextField(
                    value = url,
                    onValueChange = onUrlChange,
                    label = { Text("URL") },
                    placeholder = { Text("예: https://example.com") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "URL",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                )
            }

            // 제목 입력
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("제목 (선택사항)") },
                    placeholder = { Text("딥링크 제목을 입력하세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                )
            }

            // 카테고리 선택
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                ExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = onCategoryChange,
                        label = { Text("카테고리 (선택사항)") },
                        placeholder = { Text("카테고리를 선택하거나 입력하세요") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = MaterialTheme.shapes.small,
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false },
                    ) {
                        categories.forEach { categoryOption ->
                            DropdownMenuItem(
                                text = { Text(categoryOption) },
                                onClick = {
                                    onCategoryChange(categoryOption)
                                    isCategoryExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(space24))

            // 생성 버튼
            Button(
                onClick = onCreateDeepLink,
                enabled = url.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(space8))
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(space8))
                }
                Text("딥링크 생성")
            }
        }
    }
}

@Composable
private fun SchemeInfoCard(
    scheme: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.width(space12))
            Column {
                Text(
                    text = "선택된 스킴",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Text(
                    text = scheme,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateDeepLinkScreen() {
    CreateDeepLinkScreen(
        scheme = "https",
        url = "https://example.com",
        title = "예시 딥링크",
        category = "웹사이트",
        categories = listOf("웹사이트", "앱", "기타"),
        isLoading = false,
        onUrlChange = {},
        onTitleChange = {},
        onCategoryChange = {},
        onCreateDeepLink = {},
        onNavigateBack = {},
    )
}
