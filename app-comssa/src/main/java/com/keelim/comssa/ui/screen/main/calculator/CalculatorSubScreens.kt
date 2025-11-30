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
import kotlin.math.ceil
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
                val amount = p * (1 + r / 100).pow(t)
                // Round up to nearest integer
                val roundedAmount = ceil(amount).toLong()
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
    modifier: Modifier = Modifier,
) {
    // Simple implementation for example
    Text("대출 상환 계산기 준비 중")
}

@Composable
fun InvestmentReturnCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("투자 수익률 계산기 준비 중")
}

@Composable
fun CurrencyConverter(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("환율 변환기 준비 중")
}

@Composable
fun TaxCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("세금 계산기 준비 중")
}
