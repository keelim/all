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
package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.FeaturedData
import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.ReviewRepository
import javax.inject.Inject

class GetRandomFeatureDataUseCase @Inject constructor(
  private val dataRepository: DataRepository,
  private val reviewRepository: ReviewRepository,
) {
  suspend operator fun invoke(): FeaturedData? {
    val featuredDatas = dataRepository.getAllDatas()
      .filter { it.id.isNullOrBlank().not() }
      .filter { it.isFeatured == true }

    if (featuredDatas.isNullOrEmpty()) {
      return null
    }

    return featuredDatas.random()
      .let { data ->
        val latestReview = reviewRepository.getLatestReview(data.id!!)
        FeaturedData(data, latestReview)
      }
  }
}
