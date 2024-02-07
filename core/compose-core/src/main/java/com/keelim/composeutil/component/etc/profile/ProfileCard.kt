
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
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space24

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
        Spacer(modifier = Modifier.height(space12))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ProfileText(title = "Date", description = date)
            Icon(
                modifier = Modifier.padding(end = space24).size(size = 42.dp),
                imageVector = Icons.Rounded.Home,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(space12))
        ProfileText(title = "Time", description = time)
        Spacer(modifier = Modifier.height(space12))
        ProfileText(title = "UserName", description = username)
        Spacer(modifier = Modifier.height(space12))
        Divider(modifier = Modifier.height(space2))
        Spacer(modifier = Modifier.height(space12))
        Row(
            modifier = Modifier.fillMaxWidth().padding(space12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Image(
                //     modifier = Modifier
                //         .padding(start = space24)
                //         .size(size = 42.dp)
                //         .clip(CircleShape) ,
                //     contentScale = ContentScale.Crop,
                //     painter = painterResource(id = userImage),
                //     contentDescription = ""
                // )
                Text(
                    modifier = Modifier.padding(start = space16),
                    text = snsId,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Text(
                modifier = Modifier.padding(end = space24),
                text = userId,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun PunchHall(modifier: Modifier) {
    Canvas(modifier = modifier.border(color = Color.Magenta, width = space2)) {
        drawCircle(color = Color.Black, radius = space24.toPx())
    }
}

@Composable
private fun ProfileText(title: String, description: String) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = space24),
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            modifier = Modifier.padding(horizontal = space24),
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
