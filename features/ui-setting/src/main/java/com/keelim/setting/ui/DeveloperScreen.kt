@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.setting.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.compose.ui.ProfileImageView
import com.keelim.data.model.Developer
enum class Section {
    Developer
}
@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier,
    developer: List<Developer>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1), modifier = modifier, contentPadding = PaddingValues(10.dp)
    ) {
        items(developer) { item -> Profile(modifier = Modifier.padding(10.dp), person = item) }
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    person: Developer,
) {
    Card(
        modifier = modifier.aspectRatio(0.75f),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImageView(
                imageUrl = person.photoUrl,
                stateColor = Color(android.graphics.Color.parseColor("#43B1B3")),
                modifier = Modifier.weight(1f).aspectRatio(1f).clip(CircleShape)
            )

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = person.name,
                    color = Color(android.graphics.Color.parseColor("#FFFFFF")),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = person.companyName.orEmpty(),
                    color = Color(android.graphics.Color.parseColor("#9A9A9A")),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun DeveloperScreenPreview() {
    DeveloperScreen(
        developer = listOf(
            Developer(
                "김재현",
                photoUrl = "",
                companyName = "",
            )
        )
    )
}
@Preview(widthDp = 200)
@Composable
private fun ProfilePreview() {
    Surface(Modifier.padding(10.dp)) {
        Profile(
            person =
            Developer(
                "김재현",
                photoUrl = "",
                companyName = "",
            )
        )
    }
}
