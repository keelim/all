package com.keelim.composeutil.component.etc.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
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
