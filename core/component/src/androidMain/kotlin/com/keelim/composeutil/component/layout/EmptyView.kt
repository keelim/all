package com.keelim.composeutil.component.layout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiEmptyState
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.core.component.R

@Composable
fun EmptyView(
    text: String = stringResource(R.string.empty_view_default_title),
) {
    val backLabel = stringResource(R.string.empty_view_back_action)
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    KuiEmptyState(
        title = text,
        modifier = Modifier
            .fillMaxSize()
            .padding(space16),
        action = {
            KuiButton(onClick = { backPressDispatcher?.onBackPressed() }) {
                KuiIcon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = KuiTheme.colorScheme.onPrimary,
                )
                Spacer(modifier = Modifier.width(space8))
                KuiText(
                    text = backLabel,
                    style = KuiTheme.typography.labelLarge,
                    color = KuiTheme.colorScheme.onPrimary,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyView() {
    EmptyView()
}
