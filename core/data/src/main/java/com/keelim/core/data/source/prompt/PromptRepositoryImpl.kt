package com.keelim.core.data.source.prompt

import com.google.ai.client.generativeai.GenerativeModel
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.data.repository.PromptRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PromptRepositoryImpl @Inject constructor(
    val generativeModel: GenerativeModel,
    @Dispatcher(KeelimDispatchers.IO) val dispatcher: CoroutineDispatcher,
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
