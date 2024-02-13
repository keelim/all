package com.keelim.composeutil.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.randomColor

@Composable
fun FlipCard(
    // front
    title: String,
    name: String,
    description: String,
    // back
    title2: String,
    subtitle: String,
) {
    var isFront by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .clickable {
                isFront = isFront.not()
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(rotationY = if (isFront) 180f else 0f),
        )
        if (isFront) {
            // front
            FrontCard(
                title = title,
                name = name,
                description = description,
                color = randomColor(),
            )
        } else {
            // back
            BackCard(
                title = title2,
                subtitle = subtitle,
            )
        }
    }
}

@Composable
private fun FrontCard(
    title: String,
    name: String,
    description: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(400.dp, 200.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(space16),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = Color.Black,
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.height(space16),
                )
                Text(
                    text = title,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                )
                Spacer(
                    modifier = Modifier.height(space16),
                )
                Text(
                    text = name,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                )
                Spacer(
                    modifier = Modifier.height(space8),
                )
                Text(
                    text = description,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                )
            }
        }
    }
}

@Composable
private fun BackCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(400.dp, 200.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(space16),
        ) {
            Spacer(
                modifier = Modifier.height(space8),
            )
            Text(
                text = title,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
            )
            Spacer(
                modifier = Modifier.height(space8),
            )
            Text(
                text = subtitle,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
            )
            Spacer(
                modifier = Modifier.height(space8),
            )
            Icon(
                imageVector = Icons.Filled.AccountBox,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(48.dp),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun PreviewFlipCard() {
    FlipCard(
        title = "ullamcorper",
        name = "Meghan Roberson",
        description = "arcu",
        title2 = "elementum",
        subtitle = "natum",
    )
}

@Preview
@Composable
fun PreviewFontCard() {
    FrontCard(
        title = "ullamcorper",
        name = "Meghan Roberson",
        description = "arcu",
        color = randomColor(),
    )
}

@Preview
@Composable
fun PreviewBackCard() {
    BackCard(
        title = "elementum",
        subtitle = "natum",
    )
}
