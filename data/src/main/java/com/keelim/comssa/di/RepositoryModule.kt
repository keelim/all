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
import com.keelim.comssa.data.repository.DataRepositoryImpl
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.IoRepositoryImpl
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.data.repository.ReviewRepositoryImpl
import com.keelim.comssa.data.repository.UserRepository
import com.keelim.comssa.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
  @Binds
  abstract fun bindsIoRepository(repository: IoRepositoryImpl): IoRepository
  @Binds
  abstract fun bindsDataRepository(repository: DataRepositoryImpl): DataRepository
  @Binds
  abstract fun bindsReviewRepository(repository: ReviewRepositoryImpl): ReviewRepository
  @Binds
  abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository
}
