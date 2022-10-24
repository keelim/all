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
package com.keelim.comssa.di

import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.UserRepository
import com.keelim.comssa.domain.DeleteReviewUseCase
import com.keelim.comssa.domain.GetAllDataReviewsUseCase
import com.keelim.comssa.domain.GetAllDatasUseCase
import com.keelim.comssa.domain.GetAllReviewsUseCase
import com.keelim.comssa.domain.GetFavoriteUseCase
import com.keelim.comssa.domain.GetRandomFeatureDataUseCase
import com.keelim.comssa.domain.GetUserReviewedDataUseCase
import com.keelim.comssa.domain.SearchUseCase
import com.keelim.comssa.domain.SubmitReviewUseCase
import com.keelim.comssa.domain.UpdateFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCase {
    @Provides
    @Singleton
    fun provideSearchUseCase(
        ioRepository: IoRepository,
    ): SearchUseCase {
        return SearchUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateFavorite(
        ioRepository: IoRepository,
    ): UpdateFavoriteUseCase {
        return UpdateFavoriteUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @Singleton
    fun getFavorite(
        ioRepository: IoRepository,
    ): GetFavoriteUseCase {
        return GetFavoriteUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetAllDatasUseCase(
        dataRepository: DataRepository,
    ): GetAllDatasUseCase {
        return GetAllDatasUseCase(
            dataRepository = dataRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetRandomFeatureDataUseCase(
        dataRepository: DataRepository,
        reviewRepository: ReviewRepository,
    ): GetRandomFeatureDataUseCase {
        return GetRandomFeatureDataUseCase(
            dataRepository = dataRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetAllReviewUseCase(
        reviewRepository: ReviewRepository,
    ): GetAllReviewsUseCase {
        return GetAllReviewsUseCase(
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetUserReviewedDataUseCase(
        userRepository: UserRepository,
        dataRepository: DataRepository,
        reviewRepository: ReviewRepository,
    ): GetUserReviewedDataUseCase {
        return GetUserReviewedDataUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository,
            dataRepository = dataRepository,
        )
    }

    @Provides
    @Singleton
    fun provideDeleteReviewUseCase(
        reviewRepository: ReviewRepository,
    ): DeleteReviewUseCase {
        return DeleteReviewUseCase(
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @Singleton
    fun provideSubmitReviewUseCase(
        userRepository: UserRepository,
        reviewRepository: ReviewRepository,
    ): SubmitReviewUseCase {
        return SubmitReviewUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetAllDataReviewsUseCase(
        userRepository: UserRepository,
        reviewRepository: ReviewRepository
    ): GetAllDataReviewsUseCase {
        return GetAllDataReviewsUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository
        )
    }
}
