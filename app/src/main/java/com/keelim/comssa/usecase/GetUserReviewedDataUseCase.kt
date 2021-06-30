package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.ReviewedData
import com.keelim.comssa.data.model.User
import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.UserRepository
import javax.inject.Inject

class GetUserReviewedDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val dataRepository: DataRepository,
) {
    suspend operator fun invoke(): List<ReviewedData>{
        val user = userRepository.getUser()
        user?: kotlin.run {
            userRepository.saveUser(User())
            return emptyList()
        }
        val reviews  = reviewRepository.getAllReviews(user.id!!)
            .filter { it.dataId.isNullOrBlank().not() }

        if (reviews.isNullOrEmpty()){
            return emptyList()
        }

        return dataRepository
            .getData(reviews.map { it.dataId!! })
            .mapNotNull { data ->
                val relatedReview = reviews.find { it.dataId == data.id }
                relatedReview?.let { ReviewedData(data, it) }
            }
    }
}