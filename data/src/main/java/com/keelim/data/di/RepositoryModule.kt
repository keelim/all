package com.keelim.data.di

import com.keelim.data.source.NandaIORepository
import com.keelim.data.source.NandaIORepositoryImpl
import com.keelim.data.source.BookRepository
import com.keelim.data.source.BookRepositoryImpl
import com.keelim.data.source.DeveloperRepository
import com.keelim.data.source.DeveloperRepositoryImpl
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.DefaultTaskRepositoryImpl
import com.keelim.data.source.HistoryRepository
import com.keelim.data.source.HistoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsIoRepository(
        repository: HistoryRepositoryImpl,
    ): HistoryRepository

    @Binds
    abstract fun bindsDefaultTaskRepository(
        repository: DefaultTaskRepositoryImpl
    ): DefaultTaskRepository

    @Binds
    abstract fun bindsDeveloperRepository(
        repository: DeveloperRepositoryImpl,
    ): DeveloperRepository

    @Binds
    abstract fun bindsNandaIoRepository(
        repository: NandaIORepositoryImpl,
    ): NandaIORepository

    @Binds
    abstract fun bindsBookRepository(
        repository: BookRepositoryImpl
    ): BookRepository
}
