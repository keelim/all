package com.keelim.comssa.ui.screen.main.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiListItem
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiPrimaryScrollableTabRow
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiTab
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.core.resource.Res
import com.keelim.core.resource.calculator_history_title
import com.keelim.core.resource.calculator_result_prefix
import com.keelim.core.resource.calculator_subtitle
import com.keelim.core.resource.calculator_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorRoute(
    viewModel: CalculatorViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val history by viewModel.history.collectAsStateWithLifecycle()
    CalculatorScreen(
        history = history,
        onCalculate = viewModel::addHistory,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    history: List<CalculatorHistoryUi>,
    onCalculate: (CalculatorTypeUi, Map<String, String>, Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = CalculatorTypeUi.entries

    KuiScaffold(
        modifier = modifier,
        topBar = {
            KuiTopAppBar(
                title = {
                    Column {
                        KuiText(
                            text = stringResource(Res.string.calculator_title),
                            style = KuiTheme.typography.titleLarge,
                        )
                        KuiText(
                            text = stringResource(Res.string.calculator_subtitle),
                            style = KuiTheme.typography.bodySmall,
                            color = KuiTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            KuiPrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp,
            ) {
                tabs.forEachIndexed { index, type ->
                    KuiTab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            KuiText(
                                text = type.title,
                                style = KuiTheme.typography.labelLarge,
                            )
                        },
                    )
                }
            }

            when (tabs[selectedTabIndex]) {
                CalculatorTypeUi.COMPOUND_INTEREST -> CompoundInterestCalculator { input, result ->
                    onCalculate(CalculatorTypeUi.COMPOUND_INTEREST, input, result)
                }
                CalculatorTypeUi.LOAN_REPAYMENT -> LoanRepaymentCalculator { input, result ->
                    onCalculate(CalculatorTypeUi.LOAN_REPAYMENT, input, result)
                }
                CalculatorTypeUi.INVESTMENT_RETURN -> InvestmentReturnCalculator { input, result ->
                    onCalculate(CalculatorTypeUi.INVESTMENT_RETURN, input, result)
                }
                CalculatorTypeUi.CURRENCY_CONVERTER -> CurrencyConverter { input, result ->
                    onCalculate(CalculatorTypeUi.CURRENCY_CONVERTER, input, result)
                }
                CalculatorTypeUi.TAX -> TaxCalculator { input, result ->
                    onCalculate(CalculatorTypeUi.TAX, input, result)
                }
                CalculatorTypeUi.RETIREMENT -> RetirementCalculator { input, result ->
                    onCalculate(CalculatorTypeUi.RETIREMENT, input, result)
                }
            }

            AnimatedVisibility(visible = history.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                KuiText(
                    text = stringResource(Res.string.calculator_history_title),
                    style = KuiTheme.typography.titleMedium,
                )
                KuiHorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = history,
                        key = { it.id },
                    ) { item ->
                        KuiCard(padded = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            colors = CardDefaults.cardColors(
                                containerColor = KuiTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            KuiListItem(
                                headlineContent = {
                                    KuiText(
                                        text = item.type.title,
                                        style = KuiTheme.typography.titleMedium,
                                    )
                                },
                                supportingContent = {
                                    KuiText(
                                        text = stringResource(
                                            Res.string.calculator_result_prefix,
                                            item.result.values.joinToString(),
                                        ),
                                        style = KuiTheme.typography.bodyMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
