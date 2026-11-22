package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keelim.common.extensions.toUiNumber
import com.keelim.model.wellness.CareCheckIn
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.model.wellness.MorningCondition
import com.keelim.nandadiagnosis.R
import java.time.LocalDate

@Composable
internal fun WellnessRecordHistory(
    today: LocalDate,
    checkInsByDate: Map<String, DailyCheckIn>,
    careCheckIns: List<CareCheckIn>,
    privacyMode: Boolean,
) {
    val careCheckInsByDate = remember(careCheckIns) { careCheckIns.associateBy { it.localDate } }
    val dates = remember(today) { List(7) { today.minusDays(it.toLong()) } }
    var expandedDate by rememberSaveable(privacyMode) { mutableStateOf<String?>(null) }

    dates.forEach { date ->
        val key = date.toString()
        val careCheckIn = careCheckInsByDate[key]
        val checkIn = checkInsByDate[key]
        val hasRecord = careCheckIn != null || checkIn != null
        val canExpand = hasRecord && !privacyMode
        val expanded = canExpand && expandedDate == key
        val toggleDetails = stringResource(
            if (expanded) R.string.wellness_hide_record_details else R.string.wellness_record_details,
        )

        ListItem(
            headlineContent = {
                Text(
                    text = WellnessUiFormat.date(date),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(
                        if (hasRecord) R.string.wellness_record_present else R.string.wellness_record_missing,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            trailingContent = if (canExpand) {
                {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else null,
            modifier = if (canExpand) {
                Modifier.clickable(role = Role.Button, onClickLabel = toggleDetails) {
                    expandedDate = if (expanded) null else key
                }
            } else Modifier,
        )
        if (expanded) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (careCheckIn != null) {
                    WellnessRecordValue(
                        label = stringResource(R.string.wellness_care_weather_title),
                        value = stringResource(careCheckIn.condition.labelRes),
                    )
                    val tags = listOfNotNull(
                        R.string.wellness_care_tag_sweat.takeIf { careCheckIn.sweat },
                        R.string.wellness_care_tag_friction.takeIf { careCheckIn.friction },
                        R.string.wellness_care_tag_dryness.takeIf { careCheckIn.dryness },
                    )
                    if (tags.isNotEmpty()) {
                        WellnessRecordValue(
                            label = stringResource(R.string.wellness_record_care_tags),
                            value = tags.map { stringResource(it) }.joinToString(),
                        )
                    }
                }
                if (checkIn != null) {
                    ConditionMetric.entries.forEach { metric ->
                        WellnessRecordValue(metricLabel(metric), checkIn.valueFor(metric).toUiNumber())
                    }
                    WellnessRecordValue(
                        label = stringResource(R.string.wellness_morning_condition),
                        value = stringResource(
                            when (checkIn.morningCondition) {
                                MorningCondition.YES -> R.string.wellness_yes
                                MorningCondition.NO -> R.string.wellness_no
                                MorningCondition.NOT_CHECKED -> R.string.wellness_not_checked
                            },
                        ),
                    )
                    val observations = listOfNotNull(
                        R.string.wellness_alcohol_record.takeIf { checkIn.drankAlcohol },
                        R.string.wellness_cardio_record.takeIf { checkIn.didCardio },
                        R.string.wellness_discomfort_record.takeIf { checkIn.hasDiscomfort },
                    )
                    if (observations.isNotEmpty()) {
                        WellnessRecordValue(
                            label = stringResource(R.string.wellness_record_observations),
                            value = observations.map { stringResource(it) }.joinToString(),
                        )
                    }
                    if (checkIn.note.isNotBlank()) {
                        WellnessRecordValue(stringResource(R.string.wellness_record_note), checkIn.note)
                    }
                }
            }
        }
    }
}

@Composable
internal fun WellnessRecordValue(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
