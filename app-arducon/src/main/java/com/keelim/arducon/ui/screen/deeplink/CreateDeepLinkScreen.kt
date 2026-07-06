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
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiCircularProgressIndicator
import com.keelim.core.designsystem.component.KuiDropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTopAppBar
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateDeepLinkRoute(
    scheme: String,
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: CreateDeepLinkViewModel = hiltViewModel(),
) {
    val schemeState by viewModel.scheme.collectAsStateWithLifecycle()
    val createSuccessMessage = stringResource(Res.string.arducon_create_deeplink_success)
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
            onShowMessage(createSuccessMessage)
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

    KuiScaffold(
        topBar = {
            KuiTopAppBar(
                title = {
                    KuiText(
                        text = stringResource(Res.string.arducon_create_deeplink_title),
                        style = KuiTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                navigationIcon = {
                    KuiIconButton(onClick = onNavigateBack) {
                        KuiIcon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.arducon_back_description),
                        )
                    }
                },
            )
        },
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
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
            KuiCard(padded = false,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = KuiTheme.shapes.medium,
            ) {
                KuiOutlinedTextField(
                    value = url,
                    onValueChange = onUrlChange,
                    label = { KuiText(stringResource(Res.string.label_url)) },
                    placeholder = { KuiText(stringResource(Res.string.arducon_create_deeplink_url_placeholder)) },
                    leadingIcon = {
                        KuiIcon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(Res.string.label_url),
                            tint = KuiTheme.colorScheme.primary,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    singleLine = true,
                    shape = KuiTheme.shapes.small,
                )
            }

            // 제목 입력
            KuiCard(padded = false,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = KuiTheme.shapes.medium,
            ) {
                KuiOutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { KuiText(stringResource(Res.string.arducon_create_deeplink_title_label_optional)) },
                    placeholder = { KuiText(stringResource(Res.string.arducon_create_deeplink_title_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    singleLine = true,
                    shape = KuiTheme.shapes.small,
                )
            }

            // 카테고리 선택
            KuiCard(padded = false,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = KuiTheme.shapes.medium,
            ) {
                KuiExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    KuiOutlinedTextField(
                        value = category,
                        onValueChange = onCategoryChange,
                        label = { KuiText(stringResource(Res.string.arducon_create_deeplink_category_label_optional)) },
                        placeholder = { KuiText(stringResource(Res.string.arducon_create_deeplink_category_placeholder)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = KuiTheme.shapes.small,
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false },
                    ) {
                        categories.forEach { categoryOption ->
                            KuiDropdownMenuItem(
                                text = { KuiText(categoryOption) },
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
            KuiButton(
                onClick = onCreateDeepLink,
                enabled = url.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isLoading) {
                    KuiCircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = KuiTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(space8))
                } else {
                    KuiIcon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(space8))
                }
                KuiText(stringResource(Res.string.arducon_create_deeplink_title))
            }
        }
    }
}

@Composable
private fun SchemeInfoCard(
    scheme: String,
    modifier: Modifier = Modifier,
) {
    KuiCard(padded = false,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = KuiTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KuiIcon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = KuiTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.width(space12))
            Column {
                KuiText(
                    text = stringResource(Res.string.arducon_create_deeplink_selected_scheme),
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                KuiText(
                    text = scheme,
                    style = KuiTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = KuiTheme.colorScheme.onPrimaryContainer,
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
