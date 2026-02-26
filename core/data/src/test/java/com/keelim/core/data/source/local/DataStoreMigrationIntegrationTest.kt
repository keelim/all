package com.keelim.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.keelim.core.data.json.DefaultJsonParser
import com.keelim.core.data.repository.MarketNotificationRepositoryImpl
import com.keelim.core.data.repository.MarketNotificationStore
import com.keelim.core.data.repository.MedicationRepositoryImpl
import com.keelim.core.data.repository.MedicationStore
import com.keelim.core.data.repository.marketNotificationDataStore
import com.keelim.core.data.repository.medicationDataStore
import com.keelim.core.data.source.StationRepositoryImpl
import com.keelim.core.data.source.StationStore
import com.keelim.core.data.source.ThemeRepository
import com.keelim.core.data.source.UserPreferencesStore
import com.keelim.core.data.source.calculator.CalculatorHistoryRepositoryImpl
import com.keelim.core.data.source.calculator.CalculatorStore
import com.keelim.core.data.source.calculator.calculatorDataStore
import com.keelim.core.data.source.stationDataStore
import com.keelim.core.data.source.userPreferencesDataStore
import com.keelim.data.model.MarketSchedule
import com.keelim.data.model.Medication
import com.keelim.model.CalculatorHistory
import com.keelim.model.CalculatorType
import com.keelim.shared.data.AndroidProtoUserStateStore
import com.keelim.shared.data.UserState
import com.keelim.shared.data.model.ThemeType
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataStoreMigrationIntegrationTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val json = Json

    @Before
    fun setUp() {
        runBlocking {
            clearSharedPreferences()
            clearDataStoreFiles()

            // New store value must have the highest priority.
            seedPreferencesDataStore(UserPreferencesStore.STORE_NAME) { preferences ->
                preferences[stringPreferencesKey(UserPreferencesStore.USER_JWT_TOKEN_KEY_NAME)] =
                    "token_new_store"
            }

            // Legacy DataStore values should override SharedPreferences.
            seedPreferencesDataStore(UserPreferencesStore.LEGACY_DATASTORE_NAME) { preferences ->
                preferences[intPreferencesKey(UserPreferencesStore.USER_THEME_KEY_NAME)] = 9
                preferences[stringPreferencesKey(UserPreferencesStore.LEGACY_JWT_TOKEN_KEY_NAME)] =
                    "token_legacy_datastore"
            }

            context.getSharedPreferences(
                UserPreferencesStore.LEGACY_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE,
            ).edit()
                .putInt(UserPreferencesStore.USER_THEME_KEY_NAME, 7)
                .putString(UserPreferencesStore.LEGACY_JWT_TOKEN_KEY_NAME, "token_legacy_preference")
                .commit()

            context.getSharedPreferences(
                UserPreferencesStore.LEGACY_NANDA_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE,
            ).edit()
                .putString(UserPreferencesStore.LEGACY_NANDA_ID_TOKEN_KEY_NAME, "token_legacy_nanda")
                .commit()

            val calculatorHistory = listOf(
                CalculatorHistory(
                    id = "history_1",
                    type = CalculatorType.CURRENCY_CONVERTER,
                    input = mapOf("from" to "USD"),
                    result = mapOf("to" to "KRW"),
                    timestamp = 1_234_567L,
                ),
            )
            seedPreferencesDataStore(CalculatorStore.LEGACY_STORE_NAME) { preferences ->
                preferences[stringPreferencesKey(CalculatorStore.LEGACY_HISTORY_KEY_NAME)] =
                    json.encodeToString(calculatorHistory)
            }

            val marketSchedules = listOf(
                MarketSchedule(
                    id = "test_schedule",
                    name = "Test Market",
                    hour = 10,
                    minute = 30,
                    isEnabled = true,
                    isDefault = false,
                ),
            )
            seedPreferencesDataStore(MarketNotificationStore.LEGACY_STORE_NAME) { preferences ->
                preferences[stringPreferencesKey(MarketNotificationStore.LEGACY_SCHEDULES_KEY_NAME)] =
                    json.encodeToString(marketSchedules)
            }

            val medications = listOf(
                Medication(
                    id = "med_1",
                    name = "Vitamin C",
                    dosage = "1000mg",
                    hour = 8,
                    minute = 0,
                ),
            )
            seedPreferencesDataStore(MedicationStore.LEGACY_STORE_NAME) { preferences ->
                preferences[stringPreferencesKey(MedicationStore.LEGACY_MEDICATIONS_KEY_NAME)] =
                    json.encodeToString(medications)
            }

            seedPreferencesDataStore(StationStore.STORE_NAME) { preferences ->
                preferences[stringSetPreferencesKey(StationStore.LEGACY_FAVORITE_STATIONS_KEY_NAME)] =
                    setOf("station_a", "station_b")
            }
        }
    }

    @After
    fun tearDown() {
        clearSharedPreferences()
        clearDataStoreFiles()
    }

    @Test
    fun migratesSPToDSAndDSOldToDSNewWithDeterministicPriorityAndIdempotence() {
        runTest {
            val themeRepository = ThemeRepository(context)
            val tokenManager = TokenManager(context)
            val calculatorRepository = CalculatorHistoryRepositoryImpl(context)
            val marketRepository = MarketNotificationRepositoryImpl(context, json)
            val medicationRepository = MedicationRepositoryImpl(context, DefaultJsonParser(json))
            val stationRepository = StationRepositoryImpl(context)

            assertEquals("token_new_store", tokenManager.getToken().first())
            assertEquals(9, themeRepository.getUserTheme().first())

            val userPreferences = context.userPreferencesDataStore.data.first()
            assertEquals(
                9,
                userPreferences[intPreferencesKey(UserPreferencesStore.USER_THEME_KEY_NAME)],
            )
            assertEquals(
                "token_new_store",
                userPreferences[stringPreferencesKey(UserPreferencesStore.USER_JWT_TOKEN_KEY_NAME)],
            )
            assertEquals(
                null,
                userPreferences[stringPreferencesKey(UserPreferencesStore.LEGACY_JWT_TOKEN_KEY_NAME)],
            )

            val calculatorHistories = calculatorRepository.getAllHistory().first()
            assertEquals(1, calculatorHistories.size)
            assertEquals("history_1", calculatorHistories.first().id)

            val calculatorPreferences = context.calculatorDataStore.data.first()
            assertEquals(
                json.encodeToString(calculatorHistories),
                calculatorPreferences[stringPreferencesKey(CalculatorStore.HISTORY_KEY_NAME)],
            )
            assertEquals(
                null,
                calculatorPreferences[stringPreferencesKey(CalculatorStore.LEGACY_HISTORY_KEY_NAME)],
            )

            val schedules = marketRepository.getSchedules().first()
            assertEquals(1, schedules.size)
            assertEquals("test_schedule", schedules.first().id)

            val marketPreferences = context.marketNotificationDataStore.data.first()
            assertEquals(
                json.encodeToString(schedules),
                marketPreferences[stringPreferencesKey(MarketNotificationStore.SCHEDULES_KEY_NAME)],
            )
            assertEquals(
                null,
                marketPreferences[stringPreferencesKey(MarketNotificationStore.LEGACY_SCHEDULES_KEY_NAME)],
            )

            val medications = medicationRepository.getMedications().first()
            assertEquals(1, medications.size)
            assertEquals("med_1", medications.first().id)

            val medicationPreferences = context.medicationDataStore.data.first()
            assertEquals(
                json.encodeToString(medications),
                medicationPreferences[stringPreferencesKey(MedicationStore.MEDICATIONS_KEY_NAME)],
            )
            assertEquals(
                null,
                medicationPreferences[stringPreferencesKey(MedicationStore.LEGACY_MEDICATIONS_KEY_NAME)],
            )

            val favoriteStations = stationRepository.favoriteStations.first()
            assertEquals(setOf("station_a", "station_b"), favoriteStations)

            val stationPreferences = context.stationDataStore.data.first()
            assertEquals(
                setOf("station_a", "station_b"),
                stationPreferences[stringSetPreferencesKey(StationStore.FAVORITE_STATIONS_KEY_NAME)],
            )
            assertEquals(
                null,
                stationPreferences[stringSetPreferencesKey(StationStore.LEGACY_FAVORITE_STATIONS_KEY_NAME)],
            )

            // old DataStore files should be cleaned after successful migration.
            assertEquals(
                false,
                context.preferencesDataStoreFile(UserPreferencesStore.LEGACY_DATASTORE_NAME).exists(),
            )
            assertEquals(
                false,
                context.preferencesDataStoreFile(CalculatorStore.LEGACY_STORE_NAME).exists(),
            )
            assertEquals(
                false,
                context.preferencesDataStoreFile(MarketNotificationStore.LEGACY_STORE_NAME).exists(),
            )
            assertEquals(
                false,
                context.preferencesDataStoreFile(MedicationStore.LEGACY_STORE_NAME).exists(),
            )

            // Re-reading must keep the same values (idempotent behavior).
            assertEquals("token_new_store", tokenManager.getToken().first())
            assertEquals(9, themeRepository.getUserTheme().first())
            assertEquals(1, calculatorRepository.getAllHistory().first().size)
            assertEquals(1, marketRepository.getSchedules().first().size)
            assertEquals(1, medicationRepository.getMedications().first().size)
            assertEquals(2, stationRepository.favoriteStations.first().size)
        }
    }

    @Test
    fun migratesLegacyUserStateJsonToProtoBackedAndroidStore() {
        runTest {
            val testDirectory = File(context.cacheDir, "user_state_store_test").also { it.mkdirs() }
            val legacyJsonFile = File(testDirectory, "legacy_user_state.json")
            val protoFile = File(testDirectory, "user_state.pb")

            legacyJsonFile.writeText(
                json.encodeToString(
                    UserState(
                        isFirstUser = true,
                        visitedTime = 42,
                        themeType = ThemeType.DARK,
                    ),
                ),
            )
            protoFile.delete()

            val userStateStore = AndroidProtoUserStateStore(
                produceProtoFilePath = { protoFile.absolutePath },
                legacyJsonFilePath = { legacyJsonFile.absolutePath },
            )

            assertEquals(
                UserState(
                    isFirstUser = true,
                    visitedTime = 42,
                    themeType = ThemeType.DARK,
                ),
                userStateStore.userState.first(),
            )
            assertEquals(false, legacyJsonFile.exists())
            assertEquals(true, protoFile.exists())

            // Second read keeps migrated value with no side effects.
            assertEquals(
                UserState(
                    isFirstUser = true,
                    visitedTime = 42,
                    themeType = ThemeType.DARK,
                ),
                userStateStore.userState.first(),
            )

            protoFile.delete()
            testDirectory.delete()
        }
    }

    private suspend fun seedPreferencesDataStore(
        storeName: String,
        seedBlock: (MutablePreferences) -> Unit,
    ) {
        val scopeJob = SupervisorJob()
        val scope = CoroutineScope(scopeJob + Dispatchers.IO)
        val dataStore = PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { context.preferencesDataStoreFile(storeName) },
        )
        dataStore.edit { preferences ->
            seedBlock(preferences)
        }
        scopeJob.cancelAndJoin()
    }

    private fun clearSharedPreferences() {
        listOf(
            UserPreferencesStore.LEGACY_SHARED_PREFERENCES_NAME,
            UserPreferencesStore.LEGACY_NANDA_SHARED_PREFERENCES_NAME,
        ).forEach { sharedPreferencesName ->
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit()
        }
    }

    private fun clearDataStoreFiles() {
        listOf(
            UserPreferencesStore.STORE_NAME,
            UserPreferencesStore.LEGACY_DATASTORE_NAME,
            CalculatorStore.STORE_NAME,
            CalculatorStore.LEGACY_STORE_NAME,
            MarketNotificationStore.STORE_NAME,
            MarketNotificationStore.LEGACY_STORE_NAME,
            MedicationStore.STORE_NAME,
            MedicationStore.LEGACY_STORE_NAME,
            StationStore.STORE_NAME,
        ).forEach { storeName ->
            context.preferencesDataStoreFile(storeName).delete()
        }
    }
}
