@file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space32
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.model.DeepLink

@Composable
fun MainTopSection(
    schemeList: List<String>,
    onSearch: (String, String) -> Unit,
    onRegister: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space8),
    ) {
        val (text, setText) = remember { mutableStateOf("") }
        val (title, setTitle) = remember { mutableStateOf("") }
        val (isError, setError) = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                isError = isError,
                onValueChange = setText,
                label = { Text("please write your deeplink") },
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
            Spacer(
                modifier = Modifier.width(space8),
            )
            Icon(
                imageVector = Icons.Default.Search,
                modifier = Modifier
                    .size(space32)
                    .clickable {
                        if (text.isEmpty()) {
                            setError(true)
                        } else {
                            setError(false)
                            onSearch(text, title)
                        }
                    },
                contentDescription = "Search",
            )
        }
        Spacer(
            modifier = Modifier.height(space8),
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            isError = isError,
            onValueChange = setTitle,
            label = { Text("please write title") },
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
        Spacer(
            modifier = Modifier.height(space24),
        )
        HorizontalDivider()
        Spacer(
            modifier = Modifier.height(space24),
        )
        RegisterSchemeSection(
            schemeList = schemeList,
            setError = setError,
            setText = setText,
            onRegister = onRegister,
        )
    }
}

@Composable
fun RegisterSchemeSection(
    schemeList: List<String>,
    setError: (Boolean) -> Unit,
    setText: (String) -> Unit,
    onRegister: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (scheme, setScheme) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = scheme,
            onValueChange = setScheme,
            label = { Text("please write your scheme") },
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
        Spacer(
            modifier = Modifier.width(space8),
        )
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
    Spacer(
        modifier = Modifier.height(space8),
    )
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = space8),
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(space8),
    ) {
        schemeList.fastForEach { scheme ->
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Favorite",
                )
            }
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
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "General",
                )
            }
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
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
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
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row {
            if (deepLink.imageUrl.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier
                        .size(space64),
                    contentDescription = "",
                )
            } else {
                AsyncImage(
                    model = deepLink.imageUrl,
                    modifier = Modifier
                        .size(space64),
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space8),
            ) {
                if (deepLink.title.isNotEmpty()) {
                    Text(
                        text = deepLink.title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = deepLink.url,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(
                        modifier = Modifier.width(space8),
                    )
                    AnimatedContent(
                        targetState = deepLink.isBookMarked,
                        label = "bookmark",
                    ) { targetState ->
                        Icon(
                            imageVector = if (targetState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "bookmark",
                            modifier = Modifier.clickable {
                                onUpdate(deepLink)
                            },
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(space2),
                    )
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "play",
                        modifier = Modifier.clickable { onPlay(deepLink.url) },
                    )
                    Spacer(
                        modifier = Modifier.width(space2),
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        modifier = Modifier.clickable { onDelete(deepLink) },
                    )
                }
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
        onDelete = {},
        onUpdate = {},
    )
}
