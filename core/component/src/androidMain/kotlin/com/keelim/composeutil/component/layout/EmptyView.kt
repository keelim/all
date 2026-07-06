package com.keelim.composeutil.component.layout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space12

@Composable
fun EmptyView(
    text: String = "현재 표시 가능한 Item 이 없습니다.",
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KuiText(
            text = text,
            style = KuiTheme.typography.bodyLarge,
        )
        Spacer(
            modifier = Modifier.height(space12),
        )
        val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        KuiButton(
            onClick = { backPressDispatcher?.onBackPressed() },
        ) {
            KuiIcon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyView() {
    EmptyView()
}
