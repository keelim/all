package com.keelim.appfunction.service

import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.metadata.AppFunctionMetadata
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
class KeelimPlatformAppFunctionService : AppFunctionService() {
    private val dispatcher = KeelimAppFunctionDispatcher()

    override fun onExecuteFunction(
        request: ExecuteAppFunctionRequest,
        metadata: AppFunctionMetadata,
        cancellationSignal: CancellationSignal,
        callback: Consumer<ExecuteAppFunctionResponse>,
    ) {
        dispatcher.execute(request, metadata, cancellationSignal, callback)
    }

    override fun onDestroy() {
        dispatcher.close()
        super.onDestroy()
    }
}
