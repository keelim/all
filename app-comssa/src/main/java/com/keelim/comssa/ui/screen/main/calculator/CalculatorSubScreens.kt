package com.keelim.comssa.ui.screen.main.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiText
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
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.resource.*
import kotlin.math.pow
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompoundInterestCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val principalLabel = stringResource(Res.string.calculator_label_principal)
    val annualInterestRateLabel = stringResource(Res.string.calculator_label_annual_interest_rate)
    val periodYearsLabel = stringResource(Res.string.calculator_label_period_years)
    val calculateLabel = stringResource(Res.string.calculator_action_calculate)
    val finalAmountKey = stringResource(Res.string.calculator_key_final_amount)
    val finalAmountLabel = stringResource(Res.string.calculator_result_final_amount, result)
    val annualRateKey = stringResource(Res.string.calculator_key_annual_rate)
    val principalKey = stringResource(Res.string.calculator_key_principal)
    val periodKey = stringResource(Res.string.calculator_key_period)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        KuiOutlinedTextField(
            value = principal,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                principal = filtered.toFormattedMoneyOrEmpty()
            },
            label = { KuiText(principalLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { KuiText(annualInterestRateLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = years,
            onValueChange = { years = it },
            label = { KuiText(periodYearsLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiButton(
            onClick = {
                val p = principal.toMoneyOrZero()
                val r = rate.toDoubleOrNull() ?: 0.0
                val t = years.toDoubleOrNull() ?: 0.0
                // Simple Compound Interest: A = P(1 + r/100)^t
                val amount = p * (1 + r / 100).pow(t)
                val roundedAmount = kotlin.math.ceil(amount).toLong()
                result = roundedAmount.toUiNumber()

                onCalculate(
                    mapOf(principalKey to principal, annualRateKey to rate, periodKey to years),
                    mapOf(finalAmountKey to result),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            KuiText(calculateLabel)
        }
        if (result.isNotEmpty()) {
            KuiText(finalAmountLabel)
        }
    }
}

@Composable
fun LoanRepaymentCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    // Simple implementation for example
    KuiText(stringResource(Res.string.loan_repayment_calculator_pending))
}

@Composable
fun InvestmentReturnCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    KuiText(stringResource(Res.string.investment_return_calculator_pending))
}

@Composable
fun CurrencyConverter(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    KuiText(stringResource(Res.string.currency_converter_pending))
}

@Composable
fun TaxCalculator(
    onCalculate: (Map<String, String>, Map<String, String>) -> Unit,
) {
    KuiText(stringResource(Res.string.tax_calculator_pending))
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
    val currentAgeLabel = stringResource(Res.string.retirement_label_current_age)
    val targetAgeLabel = stringResource(Res.string.retirement_label_target_age)
    val currentSavingsLabel = stringResource(Res.string.retirement_label_current_savings)
    val annualContributionLabel = stringResource(Res.string.retirement_label_annual_contribution)
    val expectedAnnualReturnLabel = stringResource(Res.string.retirement_label_expected_annual_return)
    val calculateLabel = stringResource(Res.string.calculator_action_calculate)
    val expectedAssetResultLabel = stringResource(Res.string.retirement_result_expected_assets, result)
    val currentAgeKey = stringResource(Res.string.retirement_key_current_age)
    val retirementAgeKey = stringResource(Res.string.retirement_key_retirement_age)
    val currentSavingsKey = stringResource(Res.string.retirement_key_current_savings)
    val annualSavingsKey = stringResource(Res.string.retirement_key_annual_savings)
    val returnRateKey = stringResource(Res.string.retirement_key_return_rate)
    val expectedAssetsKey = stringResource(Res.string.retirement_key_expected_assets)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        KuiOutlinedTextField(
            value = currentAge,
            onValueChange = { currentAge = it },
            label = { KuiText(currentAgeLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = retirementAge,
            onValueChange = { retirementAge = it },
            label = { KuiText(targetAgeLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = currentSavings,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                currentSavings = filtered.toFormattedMoneyOrEmpty()
            },
            label = { KuiText(currentSavingsLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = annualContribution,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                annualContribution = filtered.toFormattedMoneyOrEmpty()
            },
            label = { KuiText(annualContributionLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        KuiOutlinedTextField(
            value = expectedReturn,
            onValueChange = { expectedReturn = it },
            label = { KuiText(expectedAnnualReturnLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )

        KuiButton(
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
                    result = roundedAmount.toUiNumber()

                    onCalculate(
                        mapOf(
                            currentAgeKey to currentAge,
                            retirementAgeKey to retirementAge,
                            currentSavingsKey to currentSavings,
                            annualSavingsKey to annualContribution,
                            returnRateKey to expectedReturn,
                        ),
                        mapOf(expectedAssetsKey to result),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            KuiText(calculateLabel)
        }
        if (result.isNotEmpty()) {
            KuiText(expectedAssetResultLabel)
        }
    }
}
