@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.HIGH
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.Holiday
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.LOW
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.MEDIUM
import com.keelim.comssa.ui.screen.main.ecocal.EcocalPriority.NONE
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
        if (entries.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "검색된 내용이 없습니다.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        } else {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    HeaderItem()
                }
                entries.forEach { (header, entries) ->
                    stickyHeader {
                        Text(
                            text = header,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = space16, vertical = space8),
                        )
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
                                        Uri.parse("https://www.google.com/search?q=${entry.country}-${entry.title} ${entry.date} ${entry.time}"),
                                    ),
                                )
                            },
                            onCountryClick = onCountryClick,
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
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${time.year} ${String.format("%02d", time.monthNumber)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${time.year}년 ${
                        String.format(
                            "%02d",
                            time.monthNumber
                        )
                    }월 ${time.dayOfMonth}일",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                )

                Row(verticalAlignment = Alignment.Bottom) {
                    AnimatedContent(
                        targetState = String.format("%02d", time.hour),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Hour Minute animation"
                    ) { targetHourMinute ->
                        Text(
                            text = targetHourMinute,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                        )
                    }
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                    )
                    AnimatedContent(
                        targetState = String.format("%02d", time.minute),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Hour Minute animation",
                    ) { targetHourMinute ->
                        Text(
                            text = targetHourMinute,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                        )
                    }
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                    )
                    AnimatedContent(
                        targetState = String.format("%02d", time.second),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "Second animation"
                    ) { second ->
                        Text(
                            text = second,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                        )
                    }
                }
            }
        }
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .padding(horizontal = space16, vertical = space8),
    ) {
        when (priority) {
            Holiday -> {
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
            else -> {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(
                    modifier = Modifier.height(space4),
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(
                    modifier = Modifier.height(space2),
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onCountryClick(label) },
                )
            }
        }

        val color = when (priority) {
            HIGH -> Color.Red
            MEDIUM -> Color.Yellow
            LOW -> Color.Green
            NONE -> Color.Transparent
            Holiday -> Color.Magenta
        }
        Box(
            modifier = Modifier
                .size(space12)
                .clip(CircleShape)
                .background(color)
                .align(Alignment.End),
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
