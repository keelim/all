package com.keelim.common.di

import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers.DEFAULT
import com.keelim.common.KeelimDispatchers.IO
import com.keelim.common.KeelimDispatchers.MAIN
import com.keelim.common.KeelimDispatchers.MAIN_IMMEDIATE
import com.keelim.common.KeelimDispatchers.UNCONFINED
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(MAIN)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Dispatcher(MAIN_IMMEDIATE)
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @Dispatcher(DEFAULT)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(UNCONFINED)
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}
