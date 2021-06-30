package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.Review
import com.keelim.comssa.data.repository.ReviewRepository
import javax.inject.Inject

class GetAllReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(dataId: String): List<Review> {
        return reviewRepository.getAllReviews(dataId)
    }
}