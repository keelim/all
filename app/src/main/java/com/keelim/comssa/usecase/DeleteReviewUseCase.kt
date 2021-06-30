package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.Review
import com.keelim.comssa.data.repository.ReviewRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(review: Review) =
        reviewRepository.removeReview(review)
}