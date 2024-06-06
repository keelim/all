@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun EcocalMainSection(
    state: LazyListState,
    entries: Map<String, List<EcoCalModel>>,
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
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.google.com/search?q=${entry.country}-${entry.title} ${entry.date} ${entry.time}"),
                                    ),
                                )
                            },
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
        modifier = modifier,
    ) {
        val rowModifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = space16, vertical = space8)

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
        Column(
            modifier = rowModifier,
        ) {
            val time = now.toLocalDateTime(timezone)
            Text(
                text = "${time.year}-${time.monthNumber}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "${time.year}년 ${time.monthNumber}월 ${time.dayOfMonth}일",
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "${time.hour}:${time.minute}:${time.second}",
            )
        }
    }
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    label: String,
    priority: EcocalPriority,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("ListItem") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = space16, vertical = space8),
    ) {
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
        )

        val color = when (priority) {
            EcocalPriority.HIGH -> Color.Red
            EcocalPriority.MEDIUM -> Color.Yellow
            EcocalPriority.LOW -> Color.Green
            EcocalPriority.NONE -> Color.Transparent
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
            priority = EcocalPriority.LOW,
            onClick = {},
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
                    priority = EcocalPriority.LOW,
                    time = "penatibus",
                    title = "option",
                ),
            )
        ),
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
    )
}
