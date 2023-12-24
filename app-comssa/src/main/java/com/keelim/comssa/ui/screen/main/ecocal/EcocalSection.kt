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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.keelim.data.model.EcoCalEntry

@Composable
fun EcocalMainSection(entries: List<EcoCalEntry>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
    ) {
        Spacer(
            modifier = Modifier.height(12.dp),
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                    onClick = {},
                )
            }
        }
    }
}

@Composable
fun HeaderItem(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier  = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val boxModifier = Modifier
                .size(12.dp)
                .clip(RectangleShape)
            Box(
                modifier = boxModifier
                    .background(Color.Red)
            )
            Text(
                text = "상",
                style = MaterialTheme.typography.bodyMedium,
            )
            Box(
                modifier = boxModifier
                    .background(Color.Yellow)
            )
            Text(
                text = "중",
                style = MaterialTheme.typography.bodyMedium,
            )
            Box(
                modifier = boxModifier
                    .background(Color.Green)
            )
            Text(
                text = "하",
                style = MaterialTheme.typography.bodyMedium,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoUrl: String? = null,
) {
    Surface(onClick = onClick, shape = MaterialTheme.shapes.large) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
        ) {
            SubcomposeAsyncImage(
                model = photoUrl,
                modifier = Modifier.size(58.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
            Column(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    // 해당 부분은 다른 composable 로 변경 예정
                    // Text(
                    //     text = priority,
                    //     style = MaterialTheme.typography.bodyMedium,
                    //     overflow = TextOverflow.Ellipsis,
                    // )
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    val color = when(priority) {
                        "상" -> Color.Red
                        "중" -> Color.Yellow
                        "하" -> Color.Green
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(color)
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
fun EcocalTopSection() {
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
