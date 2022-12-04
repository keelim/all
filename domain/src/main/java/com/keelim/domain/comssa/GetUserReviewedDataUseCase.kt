/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.domain.comssa

import com.keelim.data.model.ReviewedData
import com.keelim.data.model.User
import javax.inject.Inject

class GetUserReviewedDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val dataRepository: DataRepository,
) {
    suspend operator fun invoke(): List<ReviewedData> {
        val user = userRepository.getUser()
        user ?: kotlin.run {
            userRepository.saveUser(User())
            return emptyList()
        }
        val reviews = reviewRepository.getAllReviews(user.id!!)
            .filter { it.dataId.isNullOrBlank().not() }

        if (reviews.isNullOrEmpty()) {
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
