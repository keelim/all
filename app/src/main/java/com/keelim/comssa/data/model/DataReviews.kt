package com.keelim.comssa.data.model

data class DataReviews(
    val myReview: Review?,
    val othersReview: List<Review>
)