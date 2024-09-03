package com.keelim.setting.screen.faq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FaqRoute(
    viewModel: FaqViewModel = hiltViewModel(),
) {
    FaqScreen()
}

@Composable
fun FaqScreen() {
    LazyColumn {
        item {
            ExpandableRow(
                title = "현재 faq 준비 중입니다. ",
                subtitle = "원할한 안내를 위한 준비 중입니다. "
            )
        }
    }
}

@Composable
private fun ExpandableRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = expanded.not() }
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            val degrees by animateFloatAsState(if (expanded) -180f else 0f, label = "")
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(degrees)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                spring(
                    stiffness = Spring.StiffnessMediumLow,
                    visibilityThreshold = IntSize.VisibilityThreshold
                )
            ),
            exit = shrinkVertically()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewFaqScreen() {
    FaqScreen()
}

