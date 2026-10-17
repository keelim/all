package com.keelim.appfunction.json

import androidx.appfunctions.AppFunctionSchemaDefinition

@AppFunctionSchemaDefinition(
    category = "com.keelim.appfunction.json",
    name = "formatJson",
    version = 1,
)
interface JsonFormatterSchema {
    suspend fun formatJson(inputJson: String): String
}
