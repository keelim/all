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

import com.keelim.comssa.data.api.DataApi
import com.keelim.comssa.data.api.ReviewApi
import com.keelim.comssa.data.api.UserApi
import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.preference.PreferenceManager
import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.DataRepositoryImpl
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.IoRepositoryImpl
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.ReviewRepositoryImpl
import com.keelim.comssa.data.repository.UserRepository
import com.keelim.comssa.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Provides
  @Singleton
  fun provideIoRepository(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    db: AppDatabase,
  ): IoRepository {
    return IoRepositoryImpl(
      ioDispatcher = ioDispatcher,
      db = db
    )
  }

  @Provides
  @Singleton
  fun provideDataRepository(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    dataApi: DataApi,
  ): DataRepository {
    return DataRepositoryImpl(
      dataApi = dataApi,
      ioDispatcher = ioDispatcher
    )
  }

  @Provides
  @Singleton
  fun provideReviewRepository(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    reviewApi: ReviewApi,
  ): ReviewRepository {
    return ReviewRepositoryImpl(
      reviewApi = reviewApi,
      ioDispatcher = ioDispatcher
    )
  }

  @Provides
  @Singleton
  fun provideUserRepository(
    userApi: UserApi,
    preferenceManager: PreferenceManager,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
  ): UserRepository {
    return UserRepositoryImpl(
      userApi = userApi,
      preferenceManager = preferenceManager,
      dispatcher = ioDispatcher
    )
  }
}
