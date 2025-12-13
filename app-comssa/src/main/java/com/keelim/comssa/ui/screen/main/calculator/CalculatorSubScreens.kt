package com.keelim.comssa.ui.screen.main.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.keelim.common.extensions.toFormattedMoneyOrEmpty
import com.keelim.common.extensions.toMoneyOrZero
import java.text.DecimalFormat
import kotlin.math.pow

@Composable
fun CompoundInterestCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = principal,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                principal = filtered.toFormattedMoneyOrEmpty()
            },
            label = { Text("원금") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("연이율 (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = years,
            onValueChange = { years = it },
            label = { Text("기간 (년)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val p = principal.toMoneyOrZero()
                val r = rate.toDoubleOrNull() ?: 0.0
                val t = years.toDoubleOrNull() ?: 0.0
                // Simple Compound Interest: A = P(1 + r/100)^t
                val amount = p * (1 + r / 100).pow(t)
                val roundedAmount = kotlin.math.ceil(amount).toLong()
                result = DecimalFormat("#,###").format(roundedAmount)

                onCalculate(
                    mapOf("원금" to principal, "연이율" to rate, "기간" to years),
                    mapOf("최종 금액" to result)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("계산하기")
        }
        if (result.isNotEmpty()) {
            Text("최종 금액: $result")
        }
    }
}

@Composable
fun LoanRepaymentCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    // Simple implementation for example
    Text("대출 상환 계산기 준비 중")
}

@Composable
fun InvestmentReturnCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    Text("투자 수익률 계산기 준비 중")
}

@Composable
fun CurrencyConverter(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    Text("환율 변환기 준비 중")
}

@Composable
fun TaxCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    Text("세금 계산기 준비 중")
}

@Composable
fun RetirementCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    var currentAge by remember { mutableStateOf("") }
    var retirementAge by remember { mutableStateOf("") }
    var currentSavings by remember { mutableStateOf("") }
    var annualContribution by remember { mutableStateOf("") }
    var expectedReturn by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = currentAge,
            onValueChange = { currentAge = it },
            label = { Text("현재 나이") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = retirementAge,
            onValueChange = { retirementAge = it },
            label = { Text("은퇴 목표 나이") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = currentSavings,
            onValueChange = {
               val filtered = it.filter { char -> char.isDigit() }
               currentSavings = filtered.toFormattedMoneyOrEmpty()
            },
            label = { Text("현재 자산 (원)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = annualContribution,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                annualContribution = filtered.toFormattedMoneyOrEmpty()
            },
            label = { Text("연간 저축액 (원)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
         OutlinedTextField(
            value = expectedReturn,
            onValueChange = { expectedReturn = it },
            label = { Text("예상 연 수익률 (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val age = currentAge.toIntOrNull() ?: 0
                val retAge = retirementAge.toIntOrNull() ?: 0
                val savings = currentSavings.toMoneyOrZero()
                val contribution = annualContribution.toMoneyOrZero()
                val rate = expectedReturn.toDoubleOrNull() ?: 0.0

                if (retAge > age) {
                    val years = retAge - age
                    val r = rate / 100.0
                    // FV = PV * (1+r)^n + PMT * [((1+r)^n - 1) / r]
                    // If r = 0, FV = PV + PMT * n
                    val futureValue = if (r == 0.0) {
                        savings + contribution * years
                    } else {
                        (savings * (1 + r).pow(years)) + (contribution * (((1 + r).pow(years) - 1) / r))
                    }
                    
                    val roundedAmount = kotlin.math.ceil(futureValue).toLong()
                    result = DecimalFormat("#,###").format(roundedAmount)

                    onCalculate(
                         mapOf(
                            "현재 나이" to currentAge,
                            "은퇴 나이" to retirementAge,
                            "현재 자산" to currentSavings,
                             "연간 저축" to annualContribution,
                             "수익률" to expectedReturn
                         ),
                        mapOf("은퇴 시 예상 자산" to result)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("계산하기")
        }
        if (result.isNotEmpty()) {
            Text("은퇴 시 예상 자산: $result 원")
        }
    }
}
