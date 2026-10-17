package com.keelim.appfunction.service

import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.ExtensionsAppFunctionService
import androidx.appfunctions.metadata.AppFunctionMetadata
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class KeelimExtensionAppFunctionService : ExtensionsAppFunctionService() {
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
