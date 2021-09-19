package com.keelim.cnubus.data.repository.setting

import com.keelim.cnubus.data.model.Developer
import javax.inject.Inject

internal class DeveloperRepositoryImpl @Inject constructor(
) : DeveloperRepository {
    override suspend fun getDeveloper(): List<Developer> {
        return runCatching {
            listOf(Developer(
                "김재현",
                "https://avatars.githubusercontent.com/u/26667456?v=4",
                "김재현",
                "https://github.com/keelim"
            ))
        }.getOrDefault(listOf(Developer(
            "김재현",
            "https://avatars.githubusercontent.com/u/26667456?v=4",
            "김재현",
            "https://github.com/keelim"
        )))
    }
}