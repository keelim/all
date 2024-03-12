package com.keelim.composeutil.component.etc.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.conditional

private val fillColor = Color(0xFFFFC107)
private val emptyColor = Color(0xFFEEEEEE)

@Immutable
data class Review(
    val rating: String,
    val percentage: Int,
)

@Composable
fun ReviewsSection(
    rating: Float,
    maxRating: Int,
    totalReviews: String,
    reviews: List<Review>,
    onMoreReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(36.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .padding(space16),
    ) {
        Column(Modifier.weight(1f)) {
            reviews.fastForEach { review ->
                ReviewPercentageBar(
                    rating = review.rating,
                    percentage = review.percentage,
                )
            }
        }
        RatingSummary(
            rating = rating,
            maxRating = maxRating,
            totalReviews = totalReviews,
            onMoreReviewsClick = onMoreReviewsClick,
        )
    }
}

@Composable
private fun ReviewPercentageBar(rating: String, percentage: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space12),
    ) {
        Text(rating)
        fun rememberPercentageShape(
            percentage: Int,
            shape: Shape,
        ): Shape {
            return object : Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density,
                ): Outline {
                    val right = size.width * (percentage / 100f)
                    return shape.createOutline(Size(right, size.height), layoutDirection, density)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(space8)
                .clip(MaterialTheme.shapes.small)
                .background(emptyColor)
                .clip(
                    rememberPercentageShape(percentage, MaterialTheme.shapes.small),
                )
                .background(fillColor),
        )
    }
}

@Composable
private fun RatingSummary(
    rating: Float,
    maxRating: Int,
    totalReviews: String,
    onMoreReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp),
        )
        RatingBar(rating = rating, maxRating = maxRating)
        TextButton(onClick = onMoreReviewsClick) {
            Text("$totalReviews reviews")
        }
    }
}

@Composable
private fun RatingBar(
    rating: Float,
    maxRating: Int,
    modifier: Modifier = Modifier,
) {
    val firstHalf = remember {
        object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Outline {
                return Outline.Rectangle(Rect(0f, 0f, size.width / 2, size.height))
            }
        }
    }

    Row(
        modifier = modifier,
    ) {
        repeat(maxRating) { i ->
            Box(Modifier.size(18.dp)) {
                val lastFullIndex = (rating - 1).toInt()
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = emptyColor,
                    modifier = Modifier.matchParentSize(),
                )
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = fillColor,
                    modifier = Modifier
                        .conditional(
                            condition = i <= lastFullIndex,
                            ifTrue = { matchParentSize() },
                            ifFalse = { matchParentSize().clip(firstHalf) },
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
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
