@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import androidx.core.net.toUri
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.HIGH
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.Holiday
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.LOW
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.MEDIUM
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.NONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

@Composable
fun EcocalMainSection(
    state: LazyListState,
    entries: Map<String, List<EcoCalModel>>,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("EcocalMainSection") {
    val context = LocalContext.current
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
                HeaderItem()
            }
            entries.forEach { (header, entries) ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape,
                            )
                            .animateItem(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = header,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = space16, vertical = space8),
                        )
                    }
                }
                items(entries) { entry ->
                    ListItem(
                        title = entry.title,
                        subtitle = "${entry.date} ${entry.time}",
                        label = entry.country,
                        priority = entry.priority,
                        onCardClick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.google.com/search?q=${entry.country}-${entry.title} ${entry.date} ${entry.time}".toUri(),
                                ),
                            )
                        },
                        onCountryClick = onCountryClick,
                        modifier = Modifier.animateItem(),
                    )
                }
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
                        text = "여기가 마지막 일정입니다.",
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
fun HeaderItem(modifier: Modifier = Modifier) = trace("HeaderItem") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = space16, vertical = space16),
    ) {
        var now by remember {
            mutableStateOf(
                Clock.System.now(),
            )
        }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1000L)
                now = Clock.System.now()
            }
        }

        val timezone = remember {
            TimeZone.currentSystemDefault()
        }

        val time = now.toLocalDateTime(timezone)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "${time.year} ${String.format("%02d", time.monthNumber)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Calendar Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(space4))
                    Text(
                        text = "${time.year}년 ${
                            String.format(
                                "%02d",
                                time.monthNumber,
                            )
                        }월 ${time.dayOfMonth}일",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    AnimatedContent(
                        targetState = String.format("%02d", time.hour),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Hour animation",
                    ) { targetHour ->
                        Text(
                            text = targetHour,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    )
                    AnimatedContent(
                        targetState = String.format("%02d", time.minute),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Minute animation",
                    ) { targetMinute ->
                        Text(
                            text = targetMinute,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    )
                    AnimatedContent(
                        targetState = String.format("%02d", time.second),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Second animation",
                    ) { second ->
                        Text(
                            text = second,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(space8))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            thickness = 1.dp,
        )
    }
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    label: String,
    priority: EcocalPriority,
    onCardClick: () -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("ListItem") {
    if (priority == Holiday) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onCardClick() }
                .padding(horizontal = space16, vertical = space8),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(
                modifier = Modifier.height(space4),
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    } else {
        ListItem(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onCardClick() },
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    val color = remember(priority) {
                        when (priority) {
                            HIGH -> Color.Red
                            MEDIUM -> Color.Yellow
                            LOW -> Color.Green
                            NONE -> Color.Transparent
                            Holiday -> Color.Magenta
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(space8),
                    )
                    Box(
                        modifier = Modifier
                            .size(space12)
                            .background(color, shape = CircleShape)
                            .align(Alignment.Start),
                    )
                }
            },
            trailingContent = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onCountryClick(label) },
                )
            },
        )
    }
}

@Preview
@Composable
private fun PreviewListItem() {
    MaterialTheme {
        ListItem(
            title = "fastidii",
            subtitle = "ultrices",
            label = "efficitur",
            priority = LOW,
            onCardClick = {},
            onCountryClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}

@Preview
@Composable
private fun PreviewEcocalMainSection() {
    EcocalMainSection(
        state = rememberLazyListState(),
        entries = mapOf(
            "a" to listOf(
                EcoCalModel(
                    country = "Congo, Democratic Republic of the",
                    date = "ridiculus",
                    priority = LOW,
                    time = "penatibus",
                    title = "option",
                ),
            ),
            "b" to listOf(
                EcoCalModel(
                    country = "Congo, Democratic Republic of the",
                    date = "ridiculus",
                    priority = Holiday,
                    time = "penatibus",
                    title = "option",
                ),
            ),
        ),
        onCountryClick = {},
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
    )
}

@Composable
fun EcocalFloatingButton(
    showButton: Boolean,
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    updateFilter: (FabButtonItem) -> Unit,
) {
    val items by remember {
        mutableStateOf(
            listOf(
                High(),
                Medium(),
                Low(),
                Clear(),
            ),
        )
    }

    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        expanded = isExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = setIsExpanded,
                content = {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (isExpanded) "Close" else "Open",
                    )
                },
            )
        },
    ) {
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(),
            exit = fadeOut(),
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
        }

        items.fastForEach { item ->
            FloatingActionButtonMenuItem(
                onClick = {
                    Timber.Forest.d("item $item")
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
fun EcocalNavigationBar(
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
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "캘린더",
                )
            },
            label = {
                Text(text = "캘린더")
            },
        )

        ShortNavigationBarItem(
            selected = navigationIndex.intValue == 1,
            onClick = {
                navigationIndex.intValue = 1
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "환율",
                )
            },
            label = {
                Text(text = "환율")
            },
        )

        ShortNavigationBarItem(
            selected = navigationIndex.intValue == 2,
            onClick = {
                navigationIndex.intValue = 2
            },
            icon = {
                // Icon temporarily removed
            },
            label = {
                Text(text = "금융")
            },
        )
    }
}
