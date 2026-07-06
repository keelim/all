package com.keelim.setting.screen.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.keelim.core.designsystem.component.KuiCard
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.model.Alarm

@Composable
fun AlarmSection(
    items: List<Alarm>,
    modifier: Modifier = Modifier,
) = trace("AlarmSection") {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        NavigationBackArrowBar(title = "알림 확인")
        Spacer(
            modifier = Modifier.height(space4),
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(items) { item ->
                AlarmItem(
                    item = item,
                )
            }
        }
    }
}

@Composable
private fun AlarmItem(
    item: Alarm,
    modifier: Modifier = Modifier,
) = trace("AlarmItem") {
    KuiCard(padded = false,
        modifier = modifier
            .padding(
                horizontal = space8,
                vertical = space8,
            )
            .fillMaxWidth(),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(space16)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
            ) {
                KuiText(
                    text = item.title,
                    style = KuiTheme.typography.headlineMedium,
                )
                Spacer(
                    modifier = Modifier.height(space4),
                )
                KuiText(
                    text = item.subTitle,
                    style = KuiTheme.typography.bodyMedium,
                )
                Spacer(
                    modifier = Modifier.height(space2),
                )
                KuiText(
                    text = item.receiveDate,
                    style = KuiTheme.typography.bodySmall,
                )
            }
        }
    }
}
