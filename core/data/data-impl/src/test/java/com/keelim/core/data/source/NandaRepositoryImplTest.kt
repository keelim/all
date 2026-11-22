package com.keelim.core.data.source

import com.keelim.shared.data.database.dao.NandaDao
import com.keelim.shared.data.database.model.NandaEntity
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NandaRepositoryImplTest : FunSpec({

    lateinit var dao: NandaDao
    lateinit var repository: NandaRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        // nandaDiagnosis is a val initialized in the constructor, so the upstream
        // getNandaEntities() must be stubbed before the repository is created.
        every { dao.getNandaEntities() } returns flowOf(emptyList<NandaEntity>())
        repository = NandaRepositoryImpl(dao)
    }

    test("nandaDiagnosis는 DAO 엔티티 스트림을 도메인으로 매핑한다 (빈 목록)") {
        runTest(testDispatcher) {
            repository.nandaDiagnosis.first() shouldBe emptyList()
        }
    }

    test("getDiagnosis는 query를 DAO에 전달한다") {
        runTest(testDispatcher) {
            every { dao.getDiagnosis("열") } returns flowOf(emptyList<NandaEntity>())

            repository.getDiagnosis("열").first() shouldBe emptyList()
            verify { dao.getDiagnosis("열") }
        }
    }
})
