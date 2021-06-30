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

import android.content.Context
import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.preference.PreferenceManager
import com.keelim.comssa.data.preference.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideAppDataBase(
    @ApplicationContext context: Context
  ): AppDatabase {
    return AppDatabase.getInstance(context)
  }

  @Provides
  @Singleton
  fun providePreferenceManager(
    @ApplicationContext context: Context
  ): PreferenceManager {
    return SharedPreferenceManager(
      context = context
    )
  }
}
