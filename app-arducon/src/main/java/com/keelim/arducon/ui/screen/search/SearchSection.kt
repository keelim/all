package com.keelim.arducon.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8

@Composable
fun SchemeSearchSection(
    schemes: List<String>,
    onSchemeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (schemes.isEmpty()) {
        EmptySearchResult(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            item {
                Text(
                    text = "검색 결과 (${schemes.size}개)",
                    style = KuiTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = space16, vertical = space8),
                )
            }

            items(schemes) { scheme ->
                SchemeItem(
                    scheme = scheme,
                    onClick = { onSchemeClick(scheme) },
                )
            }
        }
    }
}

@Composable
private fun SchemeItem(
    scheme: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space16)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = KuiTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(space12))
            Column {
                Text(
                    text = scheme,
                    style = KuiTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = KuiTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(space8))
                Text(
                    text = "딥링크 생성하기",
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun EmptySearchResult(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(space16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.padding(space16),
            tint = KuiTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "검색 결과가 없습니다",
            style = KuiTheme.typography.titleMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(space8))
        Text(
            text = "다른 검색어를 입력해보세요",
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}
