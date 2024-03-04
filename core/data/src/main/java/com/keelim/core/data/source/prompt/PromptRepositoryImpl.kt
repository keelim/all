package com.keelim.core.data.source.prompt

import com.google.ai.client.generativeai.GenerativeModel
import com.keelim.common.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class PromptRepositoryImpl @Inject constructor(
    val generativeModel: GenerativeModel,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : PromptRepository {
    override suspend fun getContent(prompt: String): Result<String> {
        return runCatching {
            withContext(dispatcher) {
                val response = generativeModel.generateContent(prompt)
                response.text ?: ""
            }
        }.onFailure { throwable ->
            Timber.e(throwable)
        }
    }
}
