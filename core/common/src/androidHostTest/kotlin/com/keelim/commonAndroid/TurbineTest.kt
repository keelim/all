import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

class TurbineTest : FunSpec({
    test("turbine selaed ui state test 1") {
        runTest {
            flow {
                emit(0)
                throw Exception("Test Complete")
            }
                .asSealedUiState()
                .test {
                    awaitItem() shouldBe SealedUiState.Loading
                    awaitItem() shouldBe SealedUiState.Success(0)

                    when (val errorResult = awaitItem()) {
                        is SealedUiState.Error ->
                            errorResult.throwable?.message shouldBe "Test Complete"

                        is SealedUiState.Loading, is SealedUiState.Success -> throw IllegalStateException(
                            "Error Result 를 emit 한다.",
                        )
                    }
                    awaitComplete()
                }
        }
    }
})
