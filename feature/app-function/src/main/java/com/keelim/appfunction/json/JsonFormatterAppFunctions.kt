package com.keelim.appfunction.json

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@AppFunctionServiceEntryPoint(
    serviceName = "GeneratedJsonFormatterAppFunctionService",
    appFunctionXmlFileName = "keelim_json_functions",
)
abstract class JsonFormatterAppFunctions : AppFunctionService(), JsonFormatterSchema {
    private val handler = JsonFormatterFunctionHandler()

    /**
     * Formats a JSON value without changing application state.
     *
     * Input must be non-blank, valid JSON and at most 65,536 characters.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    override suspend fun formatJson(inputJson: String): String {
        return handler.formatJson(inputJson)
    }
}
