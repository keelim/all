@file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space32
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.model.DeepLink

@Composable
fun MainTopSection(
    schemeList: List<String>,
    onSearch: (String, String) -> Unit,
    onRegister: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space8),
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        val (text, setText) = remember { mutableStateOf("") }
        val (title, setTitle) = remember { mutableStateOf("") }
        val (isError, setError) = remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                isError = isError,
                onValueChange = setText,
                label = { Text("Deeplink URL") },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setText("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onSearch(text, title)
                        }
                    },
                ),
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                isError = isError,
                onValueChange = setTitle,
                label = { Text("Title") },
                trailingIcon = {
                    if (title.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setTitle("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )
        }

        RegisterSchemeSection(
            schemeList = schemeList,
            setError = setError,
            setText = setText,
            onRegister = onRegister,
            onDelete = onDelete,
        )
    }
}

@Composable
fun RegisterSchemeSection(
    schemeList: List<String>,
    setError: (Boolean) -> Unit,
    setText: (String) -> Unit,
    onRegister: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (scheme, setScheme) = remember { mutableStateOf("") }
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space8)
            .animateContentSize(
                animationSpec = tween(durationMillis = 300),
            ),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = scheme,
                onValueChange = setScheme,
                label = { Text("Scheme") },
                trailingIcon = {
                    if (scheme.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                setScheme("")
                            },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (scheme.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onRegister(scheme)
                        }
                    },
                ),
            )
            Spacer(modifier = Modifier.width(space8))
            Icon(
                imageVector = Icons.Default.Add,
                modifier = Modifier
                    .size(space32)
                    .clickable {
                        if (scheme.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onRegister(scheme)
                        }
                    },
                contentDescription = "Register",
            )
        }

        if (isExpanded) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                schemeList.forEach { scheme ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                            animationSpec = tween(300),
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                            animationSpec = tween(300),
                        ),
                    ) {
                        AssistChip(
                            onClick = {
                                setError(false)
                                setText("$scheme://")
                            },
                            label = { Text("$scheme://") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add $scheme",
                                    Modifier.size(AssistChipDefaults.IconSize),
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Delete $scheme",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable { onDelete(scheme) },
                                )
                            },
                        )
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space8),
            ) {
                items(schemeList) { scheme ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                            animationSpec = tween(300),
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                            animationSpec = tween(300),
                        ),
                    ) {
                        AssistChip(
                            onClick = {
                                setError(false)
                                setText("$scheme://")
                            },
                            label = { Text("$scheme://") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add $scheme",
                                    Modifier.size(AssistChipDefaults.IconSize),
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Delete $scheme",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable { onDelete(scheme) },
                                )
                            },
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (isExpanded) "Close" else "Open",
                modifier = Modifier
                    .clickable { setIsExpanded(!isExpanded) },
            )
        }
    }
}

@Composable
fun DeepLinkSection(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    schemeList: List<String>,
    onSearch: (String, String) -> Unit,
    onRegister: (String) -> Unit,
    onDeleteScheme: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        item {
            MainTopSection(
                schemeList = schemeList,
                onSearch = onSearch,
                onRegister = onRegister,
                onDelete = onDeleteScheme,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )
        }
        stickyHeader {
            Text(
                text = "Favorite",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = space8),
            )
        }
        items(
            items = favoriteItems,
            key = { it.timestamp },
        ) {
            val (isMoved, setMoved) = remember { mutableStateOf("") }
            val context = LocalContext.current
            if (isMoved.isNotEmpty()) {
                LaunchedEffect(context, isMoved) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(isMoved),
                        ),
                    )
                    setMoved("")
                }
            }
            DeepLinkItem(
                deepLink = it,
                onPlay = { uri ->
                    setMoved(uri)
                },
                onUpdate = onUpdate,
                onDelete = onDelete,
                modifier = Modifier.animateItem(
                    placementSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
        stickyHeader {
            Text(
                text = "General",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = space8),
            )
        }
        items(
            items = generalItems,
            key = { it.timestamp },
        ) {
            val (isMoved, setMoved) = remember { mutableStateOf("") }
            val context = LocalContext.current
            if (isMoved.isNotEmpty()) {
                LaunchedEffect(context, isMoved) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(isMoved),
                        ),
                    )
                    setMoved("")
                }
            }
            DeepLinkItem(
                deepLink = it,
                onPlay = { uri ->
                    setMoved(uri)
                },
                onUpdate = onUpdate,
                onDelete = onDelete,
                modifier = Modifier.animateItem(
                    placementSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
    }
}

@Composable
private fun DeepLinkItem(
    deepLink: DeepLink,
    onPlay: (String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(space8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (deepLink.imageUrl.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier
                        .size(space64)
                        .padding(space8),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                AsyncImage(
                    model = deepLink.imageUrl,
                    modifier = Modifier
                        .size(space64)
                        .clip(MaterialTheme.shapes.medium),
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = space8),
                verticalArrangement = Arrangement.spacedBy(space4),
            ) {
                if (deepLink.title.isNotEmpty()) {
                    Text(
                        text = deepLink.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = deepLink.url,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(space4),
            ) {
                AnimatedContent(
                    targetState = deepLink.isBookMarked,
                    label = "bookmark",
                ) { targetState ->
                    Icon(
                        imageVector = if (targetState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "bookmark",
                        modifier = Modifier
                            .size(space32)
                            .clickable { onUpdate(deepLink) },
                        tint = if (targetState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onPlay(deepLink.url) },
                    tint = MaterialTheme.colorScheme.primary,
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier
                        .size(space32)
                        .clickable { onDelete(deepLink) },
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainTopSection() {
    MainTopSection(
        schemeList = listOf("https", "http"),
        onSearch = { _, _ -> },
        onRegister = {},
        onDelete = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeepLinkSection() {
    DeepLinkSection(
        favoriteItems = listOf(),
        generalItems = listOf(
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232121223L,
                title = "naver",
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 23232L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232123L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 232323L,
            ),
        ),
        onUpdate = {},
        onDelete = {},
        schemeList = emptyList(),
        onSearch = { _, _ -> },
        onRegister = {},
        onDeleteScheme = {},
    )
}
