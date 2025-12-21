package com.keelim.model

import kotlinx.serialization.Serializable

@Serializable
enum class CalculatorType {
    COMPOUND_INTEREST,
    LOAN_REPAYMENT,
    INVESTMENT_RETURN,
    CURRENCY_CONVERTER,
    TAX,
    RETIREMENT,
}

@Serializable
data class CalculatorHistory(
    val id: String,
    val type: CalculatorType,
    val input: Map<String, String>,
    val result: Map<String, String>,
    val timestamp: Long,
)

