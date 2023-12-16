package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.data.source.firebase.EcoCalEntry

@Composable
fun EcocalMainSection(entries: List<EcoCalEntry>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
    ) {
        EcocalTopSection()
        Spacer(
            modifier = Modifier.height(12.dp),
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(entries) { entry ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp)) {
                    Text(text = entry.toString())
                }
                Divider(thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ColumnScope.EcocalTopSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = "Eco Cal", style = MaterialTheme.typography.headlineMedium)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = {},
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Year")
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {},
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Month")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEcocalTopSection() {
    Column { EcocalTopSection() }
}

@Preview(showBackground = true)
@Composable
fun PreviewEcocalMainSection() {
    EcocalMainSection(
        entries =             listOf(
            EcoCalEntry(
                country = "Congo, Democratic Republic of the",
                date = "ridiculus",
                priority = "mus",
                time = "penatibus",
                title = "option",
            ),
        )
    )
}
