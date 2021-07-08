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
package com.keelim.comssa.data.repository

import com.keelim.comssa.data.api.ReviewApi
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
  private val reviewApi: ReviewApi,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ReviewRepository {
  override suspend fun getLatestReview(dataId: String): Review? = withContext(ioDispatcher) {
    return@withContext reviewApi.getLatestReview(dataId = dataId)
  }

  override suspend fun getAllReviews(dataId: String): List<Review> = withContext(ioDispatcher) {
    return@withContext reviewApi.getAllReviews(dataId)
  }

  override suspend fun getAllUserReviews(userId: String): List<Review> = withContext(ioDispatcher) {
    return@withContext reviewApi.getAllUserReviews(userId)
  }

  override suspend fun addReview(review: Review): Review = withContext(ioDispatcher) {
    return@withContext reviewApi.addReview(review)
  }

  override suspend fun removeReview(review: Review) = withContext(ioDispatcher) {
    reviewApi.removeReview(review)
  }
}
