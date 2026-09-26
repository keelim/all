package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keelim.common.extensions.formatUiDate
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.domain.CheckInRules
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import java.time.LocalDate

@Composable
internal fun MorningHistory(
    today: LocalDate,
    checkIns: List<DailyCheckIn>,
    privacyMode: Boolean,
    enabled: Boolean,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2)) {
        Text(stringResource(R.string.morning_week), style = KuiTheme.typography.titleMedium,
            color = KuiTheme.colorScheme.onSurface)
        CheckInRules.recentDates(today).forEach { date ->
            val record = checkIns.firstOrNull { it.localDate == date.toString() }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(KuiTheme.spacing.space4)) {
                    Text(formatUiDate(date.year, date.monthValue, date.dayOfMonth),
                        style = KuiTheme.typography.titleSmall, color = KuiTheme.colorScheme.onSurface)
                    if (record == null) {
                        HistoryText(stringResource(R.string.morning_no_record))
                    } else {
                        if (privacyMode) {
                            HistoryText(stringResource(R.string.wellness_hidden))
                        } else {
                            val unanswered = stringResource(R.string.morning_unanswered)
                            HistoryText(stringResource(R.string.morning_energy_value, record.energy?.toUiNumber() ?: unanswered))
                            HistoryText(stringResource(R.string.morning_sleep_value, record.sleep?.toUiNumber() ?: unanswered))
                            HistoryText(stringResource(R.string.morning_erection_value, morningLabel(record.morningCondition)))
                            if (record.note.isNotBlank()) HistoryText(record.note)
                        }
                        Row {
                            TextButton(enabled = enabled, onClick = { onEdit(record.localDate) }) {
                                HistoryText(stringResource(R.string.morning_edit))
                            }
                            TextButton(enabled = enabled, onClick = { onDelete(record.localDate) }) {
                                HistoryText(stringResource(R.string.morning_delete))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryText(text: String) {
    Text(text, style = KuiTheme.typography.bodyMedium, color = KuiTheme.colorScheme.onSurfaceVariant)
}
