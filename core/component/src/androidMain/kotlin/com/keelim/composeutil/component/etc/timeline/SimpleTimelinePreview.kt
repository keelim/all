package com.keelim.composeutil.component.etc.timeline

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.util.randomColor

@Preview
@Composable
private fun PreviewSimpleTimeline() {
    SimpleTimeline(
        items = listOf(
            Timeline(
                randomColor(),
                Icons.AutoMirrored.Rounded.ArrowForward,
                "Sent $50 to John",
                "Sep 20",
            ),
            Timeline(
                randomColor(),
                Icons.AutoMirrored.Rounded.ArrowBack,
                "Received $30 from Sarah",
                "Sep 18",
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.Share,
                "Pending payment of $25 to Alex",
                "Sep 16",
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.Close,
                "Cancelled payment to Lisa",
                "Sep 14",
            ),
            Timeline(
                randomColor(),
                Icons.AutoMirrored.Rounded.ArrowForward,
                "Sent $70 to Robert",
                "Sep 12",
            ),
            Timeline(
                randomColor(),
                Icons.Rounded.CheckCircle,
                "Opened account",
                "Sep 10",
            ),
        ),
    )
}
