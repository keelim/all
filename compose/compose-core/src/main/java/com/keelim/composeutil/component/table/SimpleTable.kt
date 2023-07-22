package com.keelim.composeutil.component.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
data class Columns(
    val date: String, val name: String, val status: String,
)

private val rawData = listOf(
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant"),
    Columns(date = "deseruisse", name = "Malinda Holder", status = "habitant")
)

@Composable
fun SimpleTableScreen() {
    LazyColumn(
        Modifier.padding(8.dp)
    ) {
        item {
            TableRow()
        }
        itemsIndexed(rawData) { index, item ->
            TableRow(
                date = item.date,
                name = item.name,
                status = item.status,
                isHeader = index == 0,
                isFooter = rawData.lastIndex == index
            )
        }
    }
}

@Composable
private fun TableRow(
    date: String = "date",
    name: String = "name",
    status: String = "status",
    isHeader: Boolean = false,
    isFooter: Boolean = false
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(text = date, weight = 1f, title = true)
        TableCell(text = name, weight = 1f, title = true)
        TableCell(text = status, weight = 1f, title = true)
    }
    if (isFooter.not()) {
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(10.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        textAlign = alignment,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSimpleTableScreen() {
    SimpleTableScreen()
}
