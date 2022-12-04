/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.domain.di

import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.UserRepository
import com.keelim.data.repository.io.IORepository
import com.keelim.domain.theme.ThemeUseCase
import com.keelim.domain.setting.UserUseCase
import com.keelim.domain.comssa.DeleteReviewUseCase
import com.keelim.domain.comssa.GetAllDataReviewsUseCase
import com.keelim.domain.comssa.GetAllDatasUseCase
import com.keelim.domain.comssa.GetFavoriteUseCase
import com.keelim.domain.comssa.GetRandomFeatureDataUseCase
import com.keelim.domain.comssa.GetUserReviewedDataUseCase
import com.keelim.domain.comssa.SearchUseCase
import com.keelim.domain.comssa.SubmitReviewUseCase
import com.keelim.domain.comssa.UpdateFavoriteUseCase
import com.keelim.domain.nandadiagnosis.GetFavoriteListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaUseCase
import com.keelim.domain.nandadiagnosis.GetSearchListUseCase
import com.keelim.domain.nandadiagnosis.HistoryUseCase
import com.keelim.domain.nandadiagnosis.VideoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetNandaUseCase(ioRepository: IORepository) = GetNandaUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNandaListUseCase(ioRepository: IORepository) = GetNandaListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteListUseCase(ioRepository: IORepository) =
        GetFavoriteListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSearchListUseCase(ioRepository: IORepository) = GetSearchListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideHistoryUseCase(ioRepository: IORepository) = HistoryUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideVideoUseCase(ioRepository: IORepository) = VideoUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideSearchUseCase(
        ioRepository: IoRepository,
    ): SearchUseCase {
        return SearchUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateFavorite(
        ioRepository: IoRepository,
    ): UpdateFavoriteUseCase {
        return UpdateFavoriteUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun getFavorite(
        ioRepository: IoRepository,
    ): GetFavoriteUseCase {
        return GetFavoriteUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllDatasUseCase(
        dataRepository: DataRepository,
    ): GetAllDatasUseCase {
        return GetAllDatasUseCase(
            dataRepository = dataRepository
        )
    }

    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
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
    @ViewModelScoped
    fun provideDeleteReviewUseCase(
        reviewRepository: ReviewRepository,
    ): DeleteReviewUseCase {
        return DeleteReviewUseCase(
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
    fun provideGetAllDataReviewsUseCase(
        userRepository: UserRepository,
        reviewRepository: ReviewRepository
    ): GetAllDataReviewsUseCase {
        return GetAllDataReviewsUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideThemeUseCase(
        themeRepository: com.keelim.cnubus.data.repository.theme.ThemeRepository
    ): ThemeUseCase = ThemeUseCase(themeRepository)

    @Provides
    @ViewModelScoped
    fun provideUserUseCase(
        userRepository: com.keelim.cnubus.data.repository.setting.UserRepository
    ): UserUseCase = UserUseCase(userRepository)
}
