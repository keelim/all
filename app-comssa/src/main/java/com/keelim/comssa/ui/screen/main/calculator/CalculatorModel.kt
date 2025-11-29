package com.keelim.comssa.ui.screen.main.calculator

import com.keelim.model.CalculatorType
import kotlinx.serialization.Serializable

@Serializable
enum class CalculatorTypeUi(val title: String, val domainType: CalculatorType) {
    // 복리 계산기
    COMPOUND_INTEREST("복리 계산기", CalculatorType.COMPOUND_INTEREST),
    // 대출 상환 계산기
    LOAN_REPAYMENT("대출 상환 계산기", CalculatorType.LOAN_REPAYMENT),
    // 투자 수익률 계산기
    INVESTMENT_RETURN("투자 수익률 계산기", CalculatorType.INVESTMENT_RETURN),
    // 환율 변환기
    CURRENCY_CONVERTER("환율 변환기", CalculatorType.CURRENCY_CONVERTER),
    // 세금 계산기
    TAX("세금 계산기", CalculatorType.TAX),
}

@Serializable
data class CalculatorHistoryUi(
    val id: String,
    val type: CalculatorTypeUi,
    val input: Map<String, String>,
    val result: Map<String, String>,
    val timestamp: Long,
)
