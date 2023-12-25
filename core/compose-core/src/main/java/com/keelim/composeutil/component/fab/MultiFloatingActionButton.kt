package com.keelim.composeutil.component.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
interface FabButtonItem {
    val imageVector: ImageVector
    val label: String
}

// FabButtonMain.kt
interface FabButtonMain {
    val imageVector: ImageVector
    val iconRotate: Float?
}

private class FabButtonMainImpl(
    override val imageVector: ImageVector,
    override val iconRotate: Float?,
) : FabButtonMain

fun FabButtonMain(iconRes: ImageVector = Icons.Filled.Add, iconRotate: Float = 45f): FabButtonMain =
    FabButtonMainImpl(iconRes, iconRotate)

// FabButtonSub.kt
interface FabButtonSub {
    val iconTint: Color
    val backgroundTint: Color
}

private class FabButtonSubImpl(override val iconTint: Color, override val backgroundTint: Color) :
    FabButtonSub

fun FabButtonSub(
    backgroundTint: Color,
    iconTint: Color,
): FabButtonSub = FabButtonSubImpl(iconTint, backgroundTint)

// FabButtonState.kt
@Stable
sealed interface FabButtonState {
    data object Collapsed : FabButtonState

    data object Expand : FabButtonState

    fun isExpanded() = this == Expand

    fun toggleValue() =
        if (isExpanded()) {
            Collapsed
        } else {
            Expand
        }
}

@Composable
fun MultiSubFab(
    item: FabButtonItem,
    option: FabButtonSub,
    onClick: (item: FabButtonItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .padding(all = 8.dp),
            color = Color.White,
        )

        FloatingActionButton(
            onClick = { onClick(item) },
            modifier = Modifier.size(40.dp),
            containerColor = option.backgroundTint,
            contentColor = option.iconTint,
        ) {
            Icon(
                imageVector = item.imageVector,
                tint = option.iconTint,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun PreviewSubFab() {
    MultiSubFab(
        item = object : FabButtonItem {
            override val imageVector: ImageVector
                get() = Icons.Filled.Add
            override val label: String
                get() = "Add"
        },
        option = FabButtonSub(
            backgroundTint = Color(0xFFE91E63),
            iconTint = Color.White,
        ),
        onClick = {},
    )
}

@Composable
fun MultiMainFab(
    fabState: FabButtonState,
    items: List<FabButtonItem>,
    fabIcon: FabButtonMain,
    fabOption: FabButtonSub,
    onFabItemClicked: (FabButtonItem) -> Unit,
    stateChanged: (fabState: FabButtonState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        if (fabState.isExpanded()) {
            fabIcon.iconRotate ?: 0f
        } else {
            0f
        },
        label = "",
    )
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End,
    ) {
        // AnimatedVisibility to show or hide the sub-items when the Multi-FAB is expanded or collapsed
        AnimatedVisibility(
            visible = fabState.isExpanded(),
        ) {
            // LazyColumn to display the sub-items in a vertical list
            LazyColumn(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(items) { item ->
                    MultiSubFab(
                        item = item,
                        option = fabOption,
                        onClick = onFabItemClicked,
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier.height(12.dp),
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = {
                stateChanged(fabState.toggleValue())
            },
            containerColor = fabOption.backgroundTint,
            contentColor = fabOption.iconTint,
        ) {
            Icon(
                imageVector = fabIcon.imageVector,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
                tint = fabOption.iconTint,
            )
        }
    }
}
