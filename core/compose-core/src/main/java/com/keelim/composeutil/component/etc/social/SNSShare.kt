package com.keelim.composeutil.component.etc.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.randomColor

@Immutable
data class SocialItem(
    val imageVector: ImageVector,
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
    val onClick: () -> Unit,
)

@Immutable
data class SocialItemHolder(
    val mainItems: List<SocialItem>,
    val subItems: List<SocialItem>,
)

@Composable
fun SocialShare(
    title: String,
    items: SocialItemHolder,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space12),
                modifier = Modifier.padding(vertical = space24),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = space24),
                )
                LazyRow(
                    contentPadding = PaddingValues(space16),
                    horizontalArrangement = Arrangement.spacedBy(space24),
                ) {
                    items(
                        items = items.mainItems,
                    ) { item ->
                        LabeledIconButton(
                            imageVector = item.imageVector,
                            label = item.label,
                            onClick = item.onClick,
                            containerColor = item.containerColor,
                            contentColor = item.contentColor,
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    items(
                        items = items.subItems,
                    ) { item ->
                        LabeledIconButton(
                            imageVector = item.imageVector,
                            label = item.label,
                            onClick = item.onClick,
                            containerColor = item.containerColor,
                            contentColor = item.contentColor,
                        )
                    }
                }
            }
        },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFBDBDBD)),
        )
    }
}

@Composable
fun LabeledIconButton(
    imageVector: ImageVector,
    label: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(space64),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}

@Preview
@Composable
private fun PreviewSocialShare() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.expand()
    }
    SocialShare(
        title = "Share Preview",
        items = SocialItemHolder(
            mainItems = listOf(
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Whatsapp",
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                    onClick = {},
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Facebook",
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                    onClick = {},
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Pinboard",
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                    onClick = {},
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Video Chat",
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                    onClick = {},
                ),
            ),
            subItems = listOf(
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Share to...",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Copy Link",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Message",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Bluetooth",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "Email",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
                SocialItem(
                    imageVector = Icons.Rounded.Share,
                    label = "More",
                    onClick = {},
                    contentColor = randomColor(),
                    containerColor = randomColor(),
                ),
            ),

        ),
        scaffoldState = scaffoldState,
    )
}
