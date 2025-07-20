@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.finance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import com.keelim.core.model.finance.FinanceRssItem
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FinanceMainSection(
    state: LazyListState,
    items: List<FinanceRssItem>,
    onSourceClick: (String) -> Unit,
    onItemClick: (FinanceRssItem) -> Unit,
    modifier: Modifier = Modifier,
) = trace("FinanceMainSection") {
    Column(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier.height(space12),
        )
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                FinanceHeaderItem()
            }
            
            items(items) { item ->
                FinanceListItem(
                    item = item,
                    onItemClick = onItemClick,
                    onSourceClick = onSourceClick,
                    modifier = Modifier.animateItem(),
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "더 이상 뉴스가 없습니다.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun FinanceHeaderItem(modifier: Modifier = Modifier) = trace("FinanceHeaderItem") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = space16, vertical = space16),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "금융 뉴스",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
                Text(
                    text = "실시간 금융 정보를 확인하세요",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    ),
                )
            }
            
            // Icon temporarily removed
        }
        
        Spacer(modifier = Modifier.height(space8))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            thickness = 1.dp,
        )
    }
}

@Composable
fun FinanceListItem(
    item: FinanceRssItem,
    onItemClick: (FinanceRssItem) -> Unit,
    onSourceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("FinanceListItem") {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(horizontal = space16, vertical = space8),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    
                    Spacer(modifier = Modifier.height(space4))
                    
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
                
                if (item.isRecent) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color.Red,
                                shape = CircleShape,
                            ),
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(space12))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.source,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.clickable { onSourceClick(item.source) },
                )
                
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }
        }
    }
}

@Composable
fun FinanceFloatingButton(
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    updateFilter: (FabButtonItem) -> Unit,
    refresh: () -> Unit,
    viewModel: FinanceViewModel = hiltViewModel(),
) {
    val items = viewModel.filterButtons

    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        expanded = isExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = setIsExpanded,
                content = {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.Menu else Icons.Filled.Add,
                        contentDescription = if (isExpanded) "Close" else "Open",
                    )
                },
            )
        },
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "scroll to top",
                )
            },
            text = { },
        )

        FloatingActionButtonMenuItem(
            onClick = {
                refresh()
                Timber.d("Finance RSS refresh clicked")
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "refresh",
                )
            },
            text = { Text("새로고침") },
        )



        items.fastForEach { item ->
            FloatingActionButtonMenuItem(
                onClick = {
                    Timber.d("Finance filter clicked: ${item.label}")
                    updateFilter(item)
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.label,
                    )
                },
                text = { Text(item.label) },
            )
        }
    }
}

@Composable
fun FinanceNavigationBar(
    navigationIndex: MutableIntState,
) {
    ShortNavigationBar {
        ShortNavigationBarItem(
            selected = navigationIndex.intValue == 0,
            onClick = {
                navigationIndex.intValue = 0
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "뉴스",
                )
            },
            label = {
                Text(text = "뉴스")
            },
        )

        ShortNavigationBarItem(
            selected = navigationIndex.intValue == 1,
            onClick = {
                navigationIndex.intValue = 1
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "설정",
                )
            },
            label = {
                Text(text = "설정")
            },
        )
    }
}

@Preview
@Composable
private fun PreviewFinanceListItem() {
    MaterialTheme {
        FinanceListItem(
            item = FinanceRssItem(
                title = "삼성전자 주가 상승",
                description = "삼성전자 주가가 전일 대비 2% 상승했습니다. 이는 최근 실적 개선과 새로운 제품 출시에 대한 기대감이 반영된 것으로 분석됩니다.",
                link = "https://example.com",
                source = "한국경제",
                category = "주식"
            ),
            onItemClick = {},
            onSourceClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}

@Preview
@Composable
private fun PreviewFinanceMainSection() {
    FinanceMainSection(
        state = rememberLazyListState(),
        items = listOf(
            FinanceRssItem(
                title = "삼성전자 주가 상승",
                description = "삼성전자 주가가 전일 대비 2% 상승했습니다.",
                link = "https://example.com",
                source = "한국경제",
                category = "주식"
            ),
            FinanceRssItem(
                title = "비트코인 가격 변동",
                description = "비트코인 가격이 5만 달러를 돌파했습니다.",
                link = "https://example.com",
                source = "코인데스크",
                category = "암호화폐"
            ),
        ),
        onSourceClick = {},
        onItemClick = {},
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
    )
} 