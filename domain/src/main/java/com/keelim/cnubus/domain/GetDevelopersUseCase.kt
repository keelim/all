package com.keelim.cnubus.domain

import com.keelim.cnubus.data.model.Developer
import com.keelim.cnubus.data.repository.setting.DeveloperRepository
import com.keelim.cnubus.domain.usecase.NonParamCoroutineUseCase
import com.keelim.common.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetDevelopersUseCase @Inject constructor(
    private val conferenceRepository: DeveloperRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : NonParamCoroutineUseCase<List<Developer>>(dispatcher) {
    override suspend fun execute(): List<Developer> {
        return conferenceRepository.getDeveloper()
            .sortedBy { it.name }
    }
}