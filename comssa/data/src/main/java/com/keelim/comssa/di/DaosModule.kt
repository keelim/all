package com.keelim.comssa.di

import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.db.dao.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesSearchDao(
        database: AppDatabase,
    ): SearchDao = database.searchDao()
}
