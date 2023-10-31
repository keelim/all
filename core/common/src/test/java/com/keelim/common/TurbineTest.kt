package com.keelim.common

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.keelim.common.model.SealedUiState
import com.keelim.common.model.asSealedUiState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TurbineTest {
    @Test
    fun `turbine selaed ui state test 1`() = runTest {
        flow {
            emit(0)
            throw Exception("Test Complete")
        }
            .asSealedUiState()
            .test {
                Truth.assertThat(SealedUiState.Loading).isEqualTo(awaitItem())
                Truth.assertThat(SealedUiState.Success(0)).isEqualTo(awaitItem())

                when (val errorResult = awaitItem()) {
                    is SealedUiState.Error ->
                        Truth.assertThat("Test Complete").isEqualTo(errorResult.throwable?.message)

                    is SealedUiState.Loading, is SealedUiState.Success -> throw IllegalStateException(
                        "Error Result 를 emit 한다.",
                    )
                }
                awaitComplete()
            }
    }

}
