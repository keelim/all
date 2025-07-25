package com.keelim.composeutil.component.button

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpandableButton(
    title: String,
    subtitle: String,
    buttonHint: String,
    clickedButtonHint: String,
    onClick: () -> Unit,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "",
    )
    Surface(
        modifier = Modifier
            .padding(vertical = space4, horizontal = space8),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space24),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp)),
            ) {
                Text(
                    text = title,
                )
                Text(
                    text = subtitle,
                )
            }
            ElevatedButton(
                onClick = {
                    onClick()
                    setExpanded(expanded.not())
                },
            ) {
                Text(
                    text = if (expanded) clickedButtonHint else buttonHint,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewExpandableButton() {
    ExpandableButton(
        title = "pellentesque",
        subtitle = "parturient",
        buttonHint = "postea",
        clickedButtonHint = "periculis",
        onClick = {},
    )
}
