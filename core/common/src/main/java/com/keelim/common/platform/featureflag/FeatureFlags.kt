package com.keelim.common.platform.featureflag

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface FeatureFlag {
    val key: String
    val defaultValue: Boolean
}

interface FeatureFlagProvider {
    fun observe(flag: FeatureFlag): Flow<Boolean>
    suspend fun isEnabled(flag: FeatureFlag): Boolean
}

class DefaultFeatureFlagProvider @Inject constructor() : FeatureFlagProvider {
    override fun observe(flag: FeatureFlag): Flow<Boolean> =
        kotlinx.coroutines.flow.flowOf(flag.defaultValue)

    override suspend fun isEnabled(flag: FeatureFlag): Boolean = flag.defaultValue
}

class LocalOverrideFeatureFlagProvider(
    initialOverrides: Map<String, Boolean> = emptyMap(),
) : FeatureFlagProvider {
    private val overrides = MutableStateFlow(initialOverrides)

    override fun observe(flag: FeatureFlag): Flow<Boolean> =
        overrides.map { it[flag.key] ?: flag.defaultValue }.distinctUntilChanged()

    override suspend fun isEnabled(flag: FeatureFlag): Boolean =
        overrides.value[flag.key] ?: flag.defaultValue

    fun setOverride(flag: FeatureFlag, enabled: Boolean?) {
        overrides.value = if (enabled == null) {
            overrides.value - flag.key
        } else {
            overrides.value + (flag.key to enabled)
        }
    }
}
