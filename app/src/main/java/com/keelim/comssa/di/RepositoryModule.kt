package com.keelim.comssa.di

import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.IoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    @Singleton
    fun provideIoRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        db:AppDatabase,
    ):IoRepository{
        return IoRepositoryImpl(
            ioDispatcher = ioDispatcher,
            db = db
        )
    }
}