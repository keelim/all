package com.keelim.composeutil.component.button

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.keelim.composeutil.component.kui.KuiElevatedButton
import com.keelim.composeutil.component.kui.KuiSurface
import com.keelim.composeutil.component.kui.KuiText
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
    KuiSurface(
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
                KuiText(
                    text = title,
                )
                KuiText(
                    text = subtitle,
                )
            }
            KuiElevatedButton(
                onClick = {
                    onClick()
                    setExpanded(expanded.not())
                },
            ) {
                KuiText(
                    text = if (expanded) clickedButtonHint else buttonHint,
                )
            }
        }
    }
}
