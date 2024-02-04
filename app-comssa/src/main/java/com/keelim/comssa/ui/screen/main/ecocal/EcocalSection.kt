@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import com.keelim.data.model.EcoCalEntry
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun EcocalMainSection(
    state: LazyListState,
    entries: List<EcoCalEntry>,
    modifier: Modifier = Modifier,
) = trace("EcocalMainSection") {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
    ) {
        Spacer(
            modifier = Modifier.height(12.dp),
        )
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
        ) {
            stickyHeader {
                HeaderItem(
                    title = "2023-12",
                )
            }
            items(entries) { entry ->
                ListItem(
                    title = entry.title,
                    subtitle = "${entry.date} ${entry.time}",
                    photoUrl = null,
                    label = entry.country,
                    priority = entry.priority,
                    isToday = entry.isToday,
                    onClick = {},
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
                        text = "여기가 마지막 일정입니다.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderItem(title: String, modifier: Modifier = Modifier) = trace("HeaderItem") {
    Column(
        modifier = modifier,
    ) {
        val rowModifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp)
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
            PlainTooltipBox(tooltip = {
                Text(
                    text = "중요도를 표시하는 항목",
                )
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.tooltipAnchor(),
                ) {
                    val boxModifier = Modifier
                        .size(12.dp)
                        .clip(RectangleShape)
                    Box(
                        modifier = boxModifier
                            .background(Color.Red),
                    )
                    Text(
                        text = "상",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(
                        modifier = boxModifier
                            .background(Color.Yellow),
                    )
                    Text(
                        text = "중",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(
                        modifier = boxModifier
                            .background(Color.Green),
                    )
                    Text(
                        text = "하",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            var now by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000L)
                    now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                }
            }
            Text(
                text = "${now.year}년 ${now.monthNumber}월 ${now.dayOfMonth}일 ${now.hour}:${now.minute}:${now.second}",
            )
        }
    }
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    label: String,
    priority: String,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoUrl: String? = null,
) = trace("ListItem") {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // SubcomposeAsyncImage(
            //     model = photoUrl,
            //     modifier = Modifier.size(58.dp).clip(CircleShape),
            //     contentScale = ContentScale.Crop,
            //     contentDescription = null,
            // ) {
            //     val state = painter.state
            //     if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            //         CircularProgressIndicator()
            //     } else {
            //         SubcomposeAsyncImageContent()
            //     }
            // }
            Column(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$subtitle $label",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    val color = when (priority) {
                        "상" -> Color.Red
                        "중" -> Color.Yellow
                        "하" -> Color.Green
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(color),
                    )
                }
            }
//          Box {
//            var isExpanded by remember { mutableStateOf(false) }
//
//            IconButton(onClick = { isExpanded = true }) {
//              Icon(Icons.Rounded.MoreVert, contentDescription = "More")
//            }
//
//             DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
//               DropdownMenuItem(text = { Text("Message") }, onClick = { /*TODO*/})
//               DropdownMenuItem(text = { Text("Block") }, onClick = { /*TODO*/})
//             }
//          }
        }
    }
}

@Composable
fun EcocalTopSection() = trace("EcocalTopSection") {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = "Eco Cal", style = MaterialTheme.typography.headlineMedium)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = {},
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Year")
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {},
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Month")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEcocalTopSection() {
    Column { EcocalTopSection() }
}

@Preview(showBackground = true)
@Composable
fun PreviewEcocalMainSection() {
    EcocalMainSection(
        state = rememberLazyListState(),
        entries =
        listOf(
            EcoCalEntry(
                country = "Congo, Democratic Republic of the",
                date = "ridiculus",
                priority = "mus",
                time = "penatibus",
                title = "option",
            ),
        ),
    )
}
