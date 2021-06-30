package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.DataReviews
import com.keelim.comssa.data.model.User
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.UserRepository
import javax.inject.Inject

class GetAllDataReviewsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(dataId:String): DataReviews {
        val reviews = reviewRepository.getAllReviews(dataId)
        val user = userRepository.getUser()

        user ?: kotlin.run {
            userRepository.saveUser(User())
            return DataReviews(null, reviews)
        }

        return DataReviews(
            reviews.find { it.userId == user.id },
            reviews.filter { it.userId != user.id }
        )
    }
}