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
import com.keelim.composeutil.component.kui.KuiCard
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiMaterialTheme
import com.keelim.composeutil.component.kui.KuiText
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
fun FrontCard(
    title: String,
    name: String,
    description: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(400.dp, 200.dp)
            .clip(KuiMaterialTheme.shapes.medium)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        KuiCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(space16),
            shape = KuiMaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
            ) {
                KuiIcon(
                    imageVector = Icons.Filled.Favorite,
                    tint = Color.Black,
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.height(space16),
                )
                KuiText(
                    text = title,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                )
                Spacer(
                    modifier = Modifier.height(space16),
                )
                KuiText(
                    text = name,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                )
                Spacer(
                    modifier = Modifier.height(space8),
                )
                KuiText(
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
fun BackCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(400.dp, 200.dp)
            .clip(KuiMaterialTheme.shapes.medium)
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
            KuiText(
                text = title,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
            )
            Spacer(
                modifier = Modifier.height(space8),
            )
            KuiText(
                text = subtitle,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
            )
            Spacer(
                modifier = Modifier.height(space8),
            )
            KuiIcon(
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
