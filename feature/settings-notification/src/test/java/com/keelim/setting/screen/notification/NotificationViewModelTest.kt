package com.keelim.setting.screen.notification

import com.keelim.data.repository.NotificationRepository
import com.keelim.model.Notification as RepositoryNotification
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationViewModelTest : FunSpec({
    extensions(MainDispatcherRule())

    test("notification state starts empty before collection begins") {
        val repository = FakeNotificationRepository()
        val viewModel =
            NotificationViewModel(
                notificationRepository = repository,
            )

        viewModel.notificationState.value shouldBe NotificationState.Empty
        repository.invocationCount shouldBe 0
    }

    test("notification state partitions fixed and general items while preserving mapped fields") {
        runTest {
            val repository =
                FakeNotificationRepository(
                    notifications =
                    listOf(
                        RepositoryNotification(
                            date = "2026.03.08",
                            title = "Pinned notice",
                            desc = "Pinned description",
                            fixed = true,
                            faq = false,
                        ),
                        RepositoryNotification(
                            date = "2026.03.07",
                            title = "General notice",
                            desc = "General description",
                            fixed = false,
                            faq = true,
                        ),
                    ),
                )
            val viewModel =
                NotificationViewModel(
                    notificationRepository = repository,
                )

            val collectionJob = startCollecting(viewModel)
            advanceUntilIdle()

            when (val state = viewModel.notificationState.value) {
                NotificationState.Empty -> error("Expected notification success state")
                is NotificationState.Success -> {
                    state.fixedItems.toList() shouldBe
                        listOf(
                            Notification(
                                date = "2026.03.08",
                                title = "Pinned notice",
                                desc = "Pinned description",
                                fixed = true,
                            ),
                        )
                    state.generalItems.toList() shouldBe
                        listOf(
                            Notification(
                                date = "2026.03.07",
                                title = "General notice",
                                desc = "General description",
                                fixed = false,
                            ),
                        )
                    viewModel.notificationState.value shouldBe state
                }
            }
            repository.invocationCount shouldBe 1

            collectionJob.cancel()
        }
    }

    test("notification state stays empty when repository returns no notifications") {
        runTest {
            val repository = FakeNotificationRepository()
            val viewModel =
                NotificationViewModel(
                    notificationRepository = repository,
                )

            val collectionJob = startCollecting(viewModel)
            advanceUntilIdle()

            viewModel.notificationState.value shouldBe NotificationState.Empty
            repository.invocationCount shouldBe 1

            collectionJob.cancel()
        }
    }

    test("notification state falls back to empty when repository throws") {
        runTest {
            val repository =
                FakeNotificationRepository(
                    throwable = IllegalStateException("boom"),
                )
            val viewModel =
                NotificationViewModel(
                    notificationRepository = repository,
                )

            val collectionJob = startCollecting(viewModel)
            advanceUntilIdle()

            viewModel.notificationState.value shouldBe NotificationState.Empty
            repository.invocationCount shouldBe 1

            collectionJob.cancel()
        }
    }
})

private class FakeNotificationRepository(
    private val notifications: List<RepositoryNotification> = emptyList(),
    private val throwable: Throwable? = null,
) : NotificationRepository {
    var invocationCount: Int = 0
        private set

    override suspend fun getNotification(): List<RepositoryNotification> {
        invocationCount += 1
        throwable?.let { throw it }
        return notifications
    }
}

private fun TestScope.startCollecting(viewModel: NotificationViewModel): Job =
    backgroundScope.launch {
        viewModel.notificationState.collect { }
    }
