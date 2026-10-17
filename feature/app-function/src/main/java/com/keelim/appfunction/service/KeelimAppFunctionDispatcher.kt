package com.keelim.appfunction.service

import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import androidx.appfunctions.AppFunctionAppUnknownException
import androidx.appfunctions.AppFunctionCancelledException
import androidx.appfunctions.AppFunctionException
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.metadata.AppFunctionMetadata
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

/** Runs either legacy function ID through the SDK-appropriate service with one callback. */
internal class KeelimAppFunctionDispatcher(
    workDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val callbackExecutor: Executor = Executor { Handler(Looper.getMainLooper()).post(it) },
    private val operation: suspend (ExecuteAppFunctionRequest, AppFunctionMetadata) -> ExecuteAppFunctionResponse =
        KeelimAppFunctionRequestHandler()::execute,
) : AutoCloseable {
    private val scope = CoroutineScope(SupervisorJob() + workDispatcher)

    fun execute(
        request: ExecuteAppFunctionRequest,
        metadata: AppFunctionMetadata,
        cancellationSignal: CancellationSignal,
        callback: Consumer<ExecuteAppFunctionResponse>,
    ) {
        val delivered = AtomicBoolean(false)
        fun deliver(response: ExecuteAppFunctionResponse) {
            if (delivered.compareAndSet(false, true)) {
                callbackExecutor.execute { callback.accept(response) }
            }
        }

        if (cancellationSignal.isCanceled) {
            deliver(cancelledResponse())
            return
        }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            val response = try {
                operation(request, metadata).also { currentCoroutineContext().ensureActive() }
            } catch (_: CancellationException) {
                cancelledResponse()
            } catch (error: AppFunctionException) {
                ExecuteAppFunctionResponse.Error(error)
            } catch (error: IllegalArgumentException) {
                ExecuteAppFunctionResponse.Error(AppFunctionInvalidArgumentException(error.message))
            } catch (error: Exception) {
                ExecuteAppFunctionResponse.Error(AppFunctionAppUnknownException(error.message))
            }
            deliver(response)
        }
        cancellationSignal.setOnCancelListener { job.cancel() }
        job.invokeOnCompletion { cause ->
            // A lazy job cancelled before it starts never enters the body above.
            if (cause is CancellationException) deliver(cancelledResponse())
            // Clearing a listener synchronously inside CancellationSignal.cancel() deadlocks.
            scope.launch(NonCancellable + Dispatchers.Default) {
                cancellationSignal.setOnCancelListener(null)
            }
        }
        job.start()
    }

    override fun close() {
        scope.cancel()
    }

    private fun cancelledResponse() =
        ExecuteAppFunctionResponse.Error(AppFunctionCancelledException("App function was cancelled"))
}
