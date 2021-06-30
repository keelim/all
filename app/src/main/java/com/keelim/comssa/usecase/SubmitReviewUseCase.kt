package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.data.model.User
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.UserRepository
import javax.inject.Inject

class SubmitReviewUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(
        data: Data,
        content: String,
        score: Float
    ): Review {
        var user = userRepository.getUser()

        user ?: kotlin.run {
            userRepository.saveUser(User())
            user = userRepository.getUser()
        }

        return reviewRepository.addReview(
            Review(
                userId = user!!.id,
                dataId = data.id,
                content = content,
                score = score
            )
        )
    }
}