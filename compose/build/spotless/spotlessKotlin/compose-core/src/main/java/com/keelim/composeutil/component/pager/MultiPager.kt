@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.composeutil.component.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MultiHorizontalPager() {
    val state = rememberPagerState()
    HorizontalPager(
        pageCount = 3,
        beyondBoundsPageCount = 3,
        state = state,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) { page ->
        when (page) {
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMultiHorizontalPager() {
    MultiHorizontalPager()
}
