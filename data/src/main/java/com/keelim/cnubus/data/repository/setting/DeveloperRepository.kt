package com.keelim.cnubus.data.repository.setting

import com.keelim.cnubus.data.model.Developer

interface DeveloperRepository {
    suspend fun getDeveloper(): List<Developer>
}
