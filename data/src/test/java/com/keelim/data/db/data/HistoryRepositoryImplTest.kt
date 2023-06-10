package com.keelim.data.db.data

import FakeHistoryDao
import com.keelim.data.source.HistoryRepository
import com.keelim.data.source.HistoryRepositoryImpl
import com.keelim.data.source.local.History
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HistoryRepositoryImplTest {

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    private val localHistories = listOf(
        History(
            subject = "lacinia",
            origin = 6613,
            average = 18.19f,
            std = 20.21f,
            number = 2746,
            grade_num = 22.23f,
            grade = "dolores",
            uid = 5789
        ),
        History(
            subject = "lacinia",
            origin = 6613,
            average = 18.19f,
            std = 20.21f,
            number = 2746,
            grade_num = 22.23f,
            grade = "dolores",
            uid = 5789
        ),
    )

    private val localDataSource = FakeHistoryDao(localHistories)
    private val historyRepository: HistoryRepository = HistoryRepositoryImpl(
        localDataSource = localDataSource,
        dispatcher = testDispatcher,
        scope = testScope
    )

    @Test
    fun observeAll_histories() = runTest {
        val histories = historyRepository.observeAll().first()
        assertEquals(localHistories, histories)
    }

    @Test
    fun onHistoryCreation() = testScope.runTest {
        val history = historyRepository.create(
            localHistories[0]
        )

        val localHistories = localDataSource.observeAll().first()
        assertEquals(true, localHistories.map { it.uid.toString() }.contains(history))
    }
}
