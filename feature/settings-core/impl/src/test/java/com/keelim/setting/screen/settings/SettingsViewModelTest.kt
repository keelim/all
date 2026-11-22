package com.keelim.setting.screen.settings

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import androidx.lifecycle.viewModelScope
import com.keelim.data.json.JsonParser
import com.keelim.data.repository.FirebaseRepository
import com.keelim.model.EcoCalEntry
import com.keelim.shared.data.UserState
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.data.model.ThemeType
import com.keelim.testing.util.MainDispatcherRule
import dagger.Lazy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val jsonParser = KotlinxJsonParser()

    extension(mainDispatcherRule)

    test("uiState combines parsed family services, user state, fcm token, and appended coming soon item") {
        runTest(testDispatcher) {
            val expectedUserState = UserState(
                isFirstUser = true,
                visitedTime = 3,
                themeType = ThemeType.DARK,
            )
            val viewModel = createViewModel(
                userState = expectedUserState,
                remoteConfigString = """
                    [
                      {"title":"Arducon","imageUrl":"https://example.com/arducon.png","actionUrl":"keelim://arducon"},
                      {"title":"Nanda","imageUrl":"https://example.com/nanda.png","actionUrl":"keelim://nanda"}
                    ]
                """.trimIndent(),
                tokenResult = Result.success("fcm-token"),
                jsonParser = jsonParser,
            )

            viewModel.uiState.test {
                awaitItem() shouldBe SettingsUiState.Initialized
                advanceUntilIdle()

                val state = awaitSuccess()
                state.userState shouldBe expectedUserState
                state.fcmToken shouldBe "fcm-token"
                state.familyServices shouldContainExactly listOf(
                    FamilyService(
                        title = "Arducon",
                        imageUrl = "https://example.com/arducon.png",
                        actionUrl = "keelim://arducon",
                    ),
                    FamilyService(
                        title = "Nanda",
                        imageUrl = "https://example.com/nanda.png",
                        actionUrl = "keelim://nanda",
                    ),
                    FamilyService(
                        title = "Coming Soon",
                        actionUrl = "",
                    ),
                )

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }

    test("empty remote config still emits only the appended coming soon family service") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                remoteConfigString = "",
                tokenResult = Result.success("token"),
                jsonParser = jsonParser,
            )

            viewModel.uiState.test {
                awaitItem() shouldBe SettingsUiState.Initialized
                advanceUntilIdle()

                val state = awaitSuccess()
                state.familyServices shouldContainExactly listOf(
                    FamilyService(
                        title = "Coming Soon",
                        actionUrl = "",
                    ),
                )

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }

    test("malformed remote config falls back to only the appended coming soon family service") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                remoteConfigString = """[{"title":"Broken","actionUrl":}""",
                tokenResult = Result.success("token"),
                jsonParser = jsonParser,
            )

            viewModel.uiState.test {
                awaitItem() shouldBe SettingsUiState.Initialized
                advanceUntilIdle()

                val state = awaitSuccess()
                state.familyServices shouldContainExactly listOf(
                    FamilyService(
                        title = "Coming Soon",
                        actionUrl = "",
                    ),
                )

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }

    test("failed fcm token falls back to an empty string") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                remoteConfigString = "",
                tokenResult = Result.failure(IllegalStateException("boom")),
                jsonParser = jsonParser,
            )

            viewModel.uiState.test {
                awaitItem() shouldBe SettingsUiState.Initialized
                advanceUntilIdle()

                val state = awaitSuccess()
                state.fcmToken shouldBe ""
                state.familyServices shouldContainExactly listOf(
                    FamilyService(
                        title = "Coming Soon",
                        actionUrl = "",
                    ),
                )

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }
})

private fun createViewModel(
    userState: UserState = UserState(),
    remoteConfigString: String,
    tokenResult: Result<String>,
    jsonParser: JsonParser,
): SettingsViewModel = SettingsViewModel(
    userStateStore = TestLazy(FakeUserStateStore(userState)),
    firebaseRepository = TestLazy(
        FakeFirebaseRepository(
            remoteConfigString = remoteConfigString,
            tokenResult = tokenResult,
        ),
    ),
    jsonParser = jsonParser,
)

private class TestLazy<T>(
    private val value: T,
) : Lazy<T> {
    override fun get(): T = value
}

private class FakeUserStateStore(
    initialUserState: UserState,
) : UserStateStore {
    private val userStateFlow = MutableStateFlow(initialUserState)

    override val userState: Flow<UserState> = userStateFlow

    override suspend fun updateIsFirstUser(isFirstUser: Boolean) {
        userStateFlow.value = userStateFlow.value.copy(isFirstUser = isFirstUser)
    }

    override suspend fun updateVisitedTime() {
        userStateFlow.value = userStateFlow.value.copy(
            visitedTime = userStateFlow.value.visitedTime + 1,
        )
    }

    override val themeTypeFlow: Flow<ThemeType> = userStateFlow.map { it.themeType }

    override fun setThemeType(value: ThemeType, scope: CoroutineScope) {
        userStateFlow.value = userStateFlow.value.copy(themeType = value)
    }
}

private class FakeFirebaseRepository(
    private val remoteConfigString: String,
    private val tokenResult: Result<String>,
) : FirebaseRepository {
    override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = emptyFlow()

    override fun getFCMToken(): Flow<Result<String>> = flowOf(tokenResult)

    override suspend fun getValue(key: String): String = remoteConfigString
}

private class KotlinxJsonParser : JsonParser {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun <T> decodeFromString(
        jsonString: String,
        deserializer: DeserializationStrategy<T>,
    ): T = json.decodeFromString(deserializer, jsonString)

    override fun <T> decodeFromStringOrNull(
        jsonString: String,
        deserializer: DeserializationStrategy<T>,
    ): T? = runCatching {
        json.decodeFromString(deserializer, jsonString)
    }.getOrNull()

    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String = json.encodeToString(serializer, value)

    override fun parseToJsonElement(jsonString: String): JsonElement = json.parseToJsonElement(jsonString)

    override fun formatJson(jsonString: String): String = json.encodeToString(parseToJsonElement(jsonString))
}

private suspend fun ReceiveTurbine<SettingsUiState>.awaitSuccess(): SettingsUiState.Success {
    while (true) {
        when (val item = awaitItem()) {
            is SettingsUiState.Success -> return item
            SettingsUiState.Initialized -> Unit
        }
    }
}
