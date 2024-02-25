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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.randomColor

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
                VerticalDivider(
                    Modifier
                        .padding(vertical = space2)
                        .padding(start = (24 / 2).dp)
                        .height(space12)
                        .width(1.dp)
                        .clip(RoundedCornerShape(100))
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = space8)
            ) {
                Icon(
                    it.icon,
                    null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(space24)
                        .background(
                            color = it.tint,
                            shape = CircleShape
                        )
                        .padding(space4)
                )
                Spacer(Modifier.width(space16))
                Text(it.text)
                Spacer(Modifier.weight(1f))
                Text(
                    text = it.time,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleTimeline() {
    SimpleTimeline(
        items = listOf(
            Timeline(
                randomColor(),
                Icons.Rounded.ArrowForward,
                "Sent $50 to John",
                "Sep 20"
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.ArrowBack,
                "Received $30 from Sarah",
                "Sep 18"
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.Schedule,
                "Pending payment of $25 to Alex",
                "Sep 16"
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.Close,
                "Cancelled payment to Lisa",
                "Sep 14"
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.ArrowForward,
                "Sent $70 to Robert",
                "Sep 12"
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.CheckCircle,
                "Opened account",
                "Sep 10"
            ),
        )
    )
}
