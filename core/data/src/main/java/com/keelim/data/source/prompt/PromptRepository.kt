package com.keelim.data.source.prompt

interface PromptRepository {
    suspend fun getContent(prompt: String): Result<String>
}
