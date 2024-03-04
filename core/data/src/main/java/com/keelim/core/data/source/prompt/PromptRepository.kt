package com.keelim.core.data.source.prompt

interface PromptRepository {
    suspend fun getContent(prompt: String): Result<String>
}
