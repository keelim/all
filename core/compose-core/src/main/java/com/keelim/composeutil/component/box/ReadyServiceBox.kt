package com.keelim.composeutil.component.box

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keelim.compose.core.R

@Composable
fun ReadyServiceBox(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.ready_service),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
