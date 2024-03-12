package com.keelim.core.data.di

import com.google.ai.client.generativeai.GenerativeModel
import com.keelim.core.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PromptModule {
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GENERATIVE,
    )
}
