package com.keelim.data.repository

interface PromptRepository {
    suspend fun getContent(prompt: String): Result<String>
}
