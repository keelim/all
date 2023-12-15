
package com.keelim.composeutil.component.etc.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
data class Profile(
    val username: String,
    val snsId: String,
    val userId: String,
    val date: String,
    val time: String,
// @DrawableRes
// val userImage: Int,
// @DrawableRes
// val userQrCode: Int,
)

@Composable
fun ProfileCard(profile: Profile) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PunchHall(modifier = Modifier.align(Alignment.TopCenter))
        ProfileContent(
            date = profile.date,
            time = profile.time,
            snsId = profile.snsId,
            userId = profile.userId,
            username = profile.username,
        )
        PunchHall(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun ProfileContent(
    date: String,
    time: String,
    snsId: String,
    userId: String,
    username: String,
) {
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ProfileText(title = "Date", description = date)
            Icon(
                modifier = Modifier.padding(end = 24.dp).size(size = 42.dp),
                imageVector = Icons.Rounded.Home,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProfileText(title = "Time", description = time)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileText(title = "UserName", description = username)
        Spacer(modifier = Modifier.height(12.dp))
        Divider(modifier = Modifier.height(2.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Image(
                //     modifier = Modifier
                //         .padding(start = 24.dp)
                //         .size(size = 42.dp)
                //         .clip(CircleShape) ,
                //     contentScale = ContentScale.Crop,
                //     painter = painterResource(id = userImage),
                //     contentDescription = ""
                // )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = snsId,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = userId,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun PunchHall(modifier: Modifier) {
    Canvas(modifier = modifier.border(color = Color.Magenta, width = 2.dp)) {
        drawCircle(color = Color.Black, radius = 24.dp.toPx())
    }
}

@Composable
private fun ProfileText(title: String, description: String) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = description,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    ProfileCard(
        profile =
        Profile(
            username = "Janelle Dickson",
            snsId = "tortor",
            userId = "fringilla",
            date = "dicam",
            time = "est",
        ),
    )
}
