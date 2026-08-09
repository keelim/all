package com.keelim.composeutil.component.etc.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiMaterialTheme
import com.keelim.composeutil.component.kui.KuiText
import com.keelim.composeutil.component.kui.KuiVerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Immutable
data class Timeline(
    val tint: Color,
    val icon: ImageVector,
    val text: String,
    val time: String,
)

@Composable
fun SimpleTimeline(
    items: List<Timeline>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(space16),
        modifier = modifier,
    ) {
        itemsIndexed(items) { index, it ->
            if (index != 0) {
                KuiVerticalDivider(
                    Modifier
                        .padding(vertical = space2)
                        .padding(start = (24 / 2).dp)
                        .height(space12)
                        .width(1.dp)
                        .clip(RoundedCornerShape(100)),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = space8),
            ) {
                KuiIcon(
                    it.icon,
                    null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(space24)
                        .background(
                            color = it.tint,
                            shape = CircleShape,
                        )
                        .padding(space4),
                )
                Spacer(Modifier.width(space16))
                KuiText(it.text)
                Spacer(Modifier.weight(1f))
                KuiText(
                    text = it.time,
                    style = KuiMaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
