@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.keelim.core.designsystem.component.KuiFloatingActionButtonMenu
import com.keelim.core.designsystem.component.KuiFloatingActionButtonMenuItem
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiListItem
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.component.KuiShortNavigationBar
import com.keelim.core.designsystem.component.KuiShortNavigationBarItem
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import com.keelim.core.designsystem.component.KuiToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import androidx.core.net.toUri
import com.keelim.common.extensions.toUiTwoDigits
import com.keelim.commonAndroid.extensions.toUiDate
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.comssa.R
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
import kotlinx.datetime.number
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
            item(key = "ecocal-header") {
                HeaderItem()
            }
            entries.forEach { (header, entries) ->
                stickyHeader(key = "ecocal-date:$header") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                KuiTheme.colorScheme.primaryContainer,
                                shape = CircleShape,
                            )
                            .animateItem(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        KuiText(
                            text = header,
                            style = KuiTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = space16, vertical = space8),
                        )
                    }
                }
                items(
                    items = entries,
                    key = { entry ->
                        "${entry.date}|${entry.time}|${entry.country}|${entry.title}"
                    },
                ) { entry ->
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
                        .background(KuiTheme.colorScheme.primaryContainer),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KuiText(
                        text = "여기가 마지막 일정입니다.",
                        style = KuiTheme.typography.bodyMedium.copy(
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
            .background(KuiTheme.colorScheme.primaryContainer)
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
            KuiText(
                text = "${time.year} ${time.month.number.toUiTwoDigits()}",
                style = KuiTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = KuiTheme.colorScheme.onPrimaryContainer,
                ),
            )
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    KuiIcon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Calendar Icon",
                        tint = KuiTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(space4))
                    KuiText(
                        text = time.toUiDate(),
                        style = KuiTheme.typography.bodyMedium.copy(
                            color = KuiTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }

                KuiText(
                    text = buildString {
                        append(time.hour.toUiTwoDigits())
                        append(':')
                        append(time.minute.toUiTwoDigits())
                        append(':')
                        append(time.second.toUiTwoDigits())
                    },
                    style = KuiTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = KuiTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.height(space8))
        KuiHorizontalDivider(
            color = KuiTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
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
        KuiCard(padded = false,
            modifier = modifier
                .fillMaxWidth()
                .clickable { onCardClick() }
                .padding(horizontal = space16, vertical = space8),
        ) {
            KuiText(
                text = title,
                style = KuiTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(
                modifier = Modifier.height(space4),
            )
            KuiText(
                text = subtitle,
                style = KuiTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(space8))
            PriorityStatus(priority = priority)
        }
    } else {
        KuiListItem(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onCardClick() },
            headlineContent = {
                KuiText(
                    text = title,
                    style = KuiTheme.typography.headlineSmall,
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    KuiText(
                        text = subtitle,
                        style = KuiTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(space8))
                    PriorityStatus(
                        priority = priority,
                        modifier = Modifier.align(Alignment.Start),
                    )
                }
            },
            trailingContent = {
                KuiTextButton(onClick = { onCountryClick(label) }) {
                    KuiText(
                        text = label,
                        style = KuiTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = KuiTheme.colorScheme.primary,
                    )
                }
            },
        )
    }
}

@Composable
private fun PriorityStatus(
    priority: EcocalPriority,
    modifier: Modifier = Modifier,
) {
    val label = stringResource(
        when (priority) {
            HIGH -> R.string.ecocal_priority_high
            MEDIUM -> R.string.ecocal_priority_medium
            LOW -> R.string.ecocal_priority_low
            NONE -> R.string.ecocal_priority_none
            Holiday -> R.string.ecocal_priority_holiday
        },
    )
    val color = when (priority) {
        HIGH -> KuiTheme.colorScheme.error
        MEDIUM -> KuiTheme.colors.warning
        LOW -> KuiTheme.colors.success
        NONE -> KuiTheme.colorScheme.onSurfaceVariant
        Holiday -> KuiTheme.colors.info
    }
    val icon = when (priority) {
        LOW -> Icons.Filled.CheckCircle
        Holiday -> Icons.Filled.DateRange
        HIGH, MEDIUM, NONE -> Icons.Filled.Info
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuiIcon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp),
        )
        KuiText(
            text = label,
            style = KuiTheme.typography.labelMedium,
            color = color,
        )
    }
}

@Preview
@Composable
private fun PreviewListItem() {
    KeelimDesignSystemTheme {
        ListItem(
            title = "fastidii",
            subtitle = "ultrices",
            label = "efficitur",
            priority = LOW,
            onCardClick = {},
            onCountryClick = {},
            modifier = Modifier.background(KuiTheme.colorScheme.surface),
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
        modifier = Modifier.background(KuiTheme.colorScheme.surface),
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

    KuiFloatingActionButtonMenu(
        expanded = isExpanded,
        button = {
            KuiToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = setIsExpanded,
                content = {
                    KuiIcon(
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
            KuiFloatingActionButtonMenuItem(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                icon = {
                    KuiIcon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "scroll to top",
                    )
                },
                text = { },
            )
        }

        items.fastForEach { item ->
            KuiFloatingActionButtonMenuItem(
                onClick = {
                    Timber.Forest.d("item $item")
                    updateFilter(item)
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                },
                icon = {
                    KuiIcon(
                        imageVector = item.imageVector,
                        contentDescription = item.label,
                    )
                },
                text = { KuiText(item.label) },
            )
        }
    }
}

@Composable
fun EcocalNavigationBar(
    navigationIndex: MutableIntState,
) {
    KuiShortNavigationBar {
        KuiShortNavigationBarItem(
            selected = navigationIndex.intValue == 0,
            onClick = {
                navigationIndex.intValue = 0
            },
            icon = {
                KuiIcon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "캘린더",
                )
            },
            label = {
                KuiText(text = "캘린더")
            },
        )

        KuiShortNavigationBarItem(
            selected = navigationIndex.intValue == 1,
            onClick = {
                navigationIndex.intValue = 1
            },
            icon = {
                KuiIcon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "환율",
                )
            },
            label = {
                KuiText(text = "환율")
            },
        )

        KuiShortNavigationBarItem(
            selected = navigationIndex.intValue == 2,
            onClick = {
                navigationIndex.intValue = 2
            },
            icon = {
                // Icon temporarily removed
            },
            label = {
                KuiText(text = "금융")
            },
        )
    }
}
