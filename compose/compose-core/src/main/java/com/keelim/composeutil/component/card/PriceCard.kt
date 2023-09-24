package com.keelim.composeutil.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
data class PriceCardState(
    val value: Float,
    val suffix: String,
    val previews: List<Pair<String, Float>>,
)

@Composable
fun PriceCard(
    priceCardState: PriceCardState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
            .fillMaxWidth()
            .height(350.dp),
        shape = RoundedCornerShape(20),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .drawBehind {
                    drawCurvyLine()
                },
        ) {
            Column(
                modifier = Modifier.padding(vertical = 50.dp, horizontal = 30.dp),
            ) {
                Text(
                    text = "My Crypto Cap",
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraLight,
                )

                Text(
                    text = "${priceCardState.value} ${priceCardState.suffix}",
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                )

                DailyPreview(
                    previews = priceCardState.previews,
                )
            }
        }
    }
}

@Composable
private fun DailyPreview(
    previews: List<Pair<String, Float>>,
) {
    val maxValue = previews.maxBy { (_, value) -> value }.second
    Row(
        modifier = Modifier.fillMaxHeight(.8f),
        verticalAlignment = Alignment.Bottom,
    ) {
        for (item in previews) {
            val columnHeightWeight =
                item.second / maxValue // <-- We use it to get a value between 0 and 1
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(columnHeightWeight) // <-- Pass it through the modifier to determine Its height
                    .padding(5.dp),
                shape = RoundedCornerShape(30),
            ) {
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (item in previews) {
            Text(
                modifier = Modifier.weight(1f),
                text = item.first,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun DrawScope.drawCurvyLine() {
    val stroke = Path().apply {
        moveTo(size.width.times(.9f), size.height.times(0f))

        quadraticBezierTo(
            size.width.times(.9f),
            size.height.times(.28f),
            size.width.times(.73f),
            size.height.times(.15f),
        )
    }

    drawPath(
        stroke,
        color = Color.White,
        style = Stroke(
            width = 50f,
            cap = StrokeCap.Round,
        ),
    )
}

@Preview
@Composable
private fun PreviewPriceCard() {
    PriceCard(
        priceCardState = PriceCardState(
            value = 2.3f,
            suffix = "adolescens",
            previews = listOf(
                Pair("Jan", 15000f),
                Pair("Feb", 20000f),
                Pair("Mar", 38000f),
                Pair("Apr", 8000f),
                Pair("May", 10000f),
            ),
        ),
    )
}
