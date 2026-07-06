@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.nandadiagnosis.ui.screen.length

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.chart.LengthChartPoint
import com.keelim.composeutil.component.chart.LengthLineChart
import com.keelim.model.LengthRecord
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDate

@Composable
fun LengthScreen(
    viewModel: LengthViewModel = hiltViewModel(),
) {
    var input by remember { mutableStateOf("") }
    val records by viewModel.records.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchRecords()
    }

    val chartPoints = records.map { LengthChartPoint(date = it.date, value = it.length) }

    Column(modifier = Modifier.padding(16.dp)) {
        // 입력 영역
        Row(verticalAlignment = Alignment.CenterVertically) {
            KuiFilledTextField(
                value = input,
                onValueChange = { input = it },
                label = { KuiText(stringResource(Res.string.nanda_length_unit_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            KuiButton(onClick = {
                val value = input.toFloatOrNull()
                if (value != null) {
                    viewModel.addRecord(
                        LengthRecord(
                            date = LocalDate.now().toString(),
                            length = value,
                        ),
                    )
                    input = ""
                }
            }) {
                KuiText(stringResource(Res.string.common_action_save))
            }
        }
        Spacer(Modifier.height(24.dp))

        // 그래프
        LengthLineChart(
            mainColor = KuiTheme.colorScheme.primary,
            subColor = KuiTheme.colorScheme.primary,
            points = chartPoints,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp))

        // 기록 리스트
        KuiText(stringResource(Res.string.nanda_length_records_title), style = KuiTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(
                items = records,
                key = { it.date }
            ) { record ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .animateItem(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    KuiText(text = stringResource(Res.string.nanda_length_record_item, record.date, record.length.toUiNumber()))
                    KuiIconButton(onClick = { viewModel.deleteRecord(record.date) }) {
                        KuiIcon(Icons.Default.Delete, contentDescription = stringResource(Res.string.nanda_length_delete))
                    }
                }
            }
        }
    }
}
