package com.keelim.composeutil.component.row

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Stable
data class RichRowInfo(
  val iconUri: String,
  val title: String,
  val subTitle: String,
  val valueChanges: List<Float>,
  val currentValue: Float,
  val totalValue: Float,
)

@Composable
fun RichCard(richRowInfo: RichRowInfo) {
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(all = 4.dp)) {
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(15.dp).fillMaxWidth()
    ) {
      RichIcon(iconUri = richRowInfo.iconUri)
      RichTitle(title = richRowInfo.title, subTitle = richRowInfo.subTitle)
      RichChart(list = richRowInfo.valueChanges)
      RichValue(currentValue = richRowInfo.currentValue, totalValue = richRowInfo.totalValue)
    }
  }
}

@Composable
private fun RichIcon(iconUri: String) {
  Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
    Canvas(
      modifier = Modifier,
      onDraw = {
        val radius = 65f
        drawCircle(color = Color.White, radius = radius)
      }
    )
    AsyncImage(
      model = iconUri,
      contentDescription = null,
      modifier = Modifier.size(25.dp).padding(2.dp)
    )
  }
}

@Preview
@Composable
private fun PreviewRichIcon() {
  RichIcon(
    iconUri =
      "https://images.unsplash.com/photo-1694671702548-0f78d62ecadc?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2574&q=80"
  )
}

@Composable
private fun RichTitle(
  title: String,
  subTitle: String,
) {
  Column(modifier = Modifier.padding(start = 10.dp, end = 5.dp).width(80.dp)) {
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = Color.Black,
      overflow = TextOverflow.Ellipsis,
      maxLines = 2
    )
    Text(text = subTitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRichTitle() {
  RichTitle(title = "malesuada", subTitle = "habitasse")
}

@Composable
fun RichValue(currentValue: Float, totalValue: Float) {
  Column(modifier = Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End) {
    Text(
      text = "$currentValue",
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = Color.Black
    )
    Text(
      text = "won ${totalValue.toInt()}",
      style = MaterialTheme.typography.labelSmall,
      color = Color.Gray
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRichValue() {
  RichValue(currentValue = 12.13f, totalValue = 14.15f)
}

private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
  (value - min) / (max - min)

@Composable
fun RichChart(modifier: Modifier = Modifier, list: List<Float>) {
  val zipList: List<Pair<Float, Float>> = list.zipWithNext()

  Row(modifier = modifier) {
    val max = list.max()
    val min = list.min()

    val lineColor =
      if (list.last() > list.first()) Color.Red
      else Color.Blue // <-- Line color is Green if its going up and Red otherwise

    for (pair in zipList) {

      val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
      val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

      Canvas(
        modifier = Modifier.fillMaxHeight().weight(1f),
        onDraw = {
          val fromPoint =
            Offset(
              x = 0f,
              y = size.height.times(1 - fromValuePercentage)
            ) // <-- Use times so it works for any available space
          val toPoint =
            Offset(x = size.width, y = size.height.times(1 - toValuePercentage)) // <-- Also here!

          drawLine(color = lineColor, start = fromPoint, end = toPoint, strokeWidth = 3f)
        }
      )
    }
  }
}

@Preview
@Composable
private fun PreviewRichChart() {
  RichChart(list = listOf(10f, 20f, 30f, 1f, 5f))
}

@Preview(showBackground = true)
@Composable
private fun PreviewRichCard() {
  RichCard(
    richRowInfo =
      RichRowInfo(
        iconUri =
          "https://images.unsplash.com/photo-1694671702548-0f78d62ecadc?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2574&q=80",
        title = "dictumst",
        subTitle = "debet",
        valueChanges = listOf(10f, 20f, 30f, 1f, 5f),
        currentValue = 4.5f,
        totalValue = 6.7f
      )
  )
}
