package com.keelim.composeutil.component.etc.review

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewReviewsSection() {
    ReviewsSection(
        rating = 4.5f,
        maxRating = 5,
        totalReviews = "3",
        reviews = listOf(
            Review("5", 46),
            Review("4", 28),
            Review("3", 13),
            Review("2", 9),
            Review("1", 4),
        ),
        onMoreReviewsClick = { },
    )
}
