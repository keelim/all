package com.keelim.nandadiagnosis.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainBottomSheetRoute(
    onBlogClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    MainBottomSheetScreen(
        onBlogClick = onBlogClick,
        onFavoriteClick = onFavoriteClick,
        onOpenSourceClick = onOpenSourceClick,
        onAboutClick = onAboutClick,
    )
}

@Composable
private fun MainBottomSheetScreen(
    onBlogClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onOpenSourceClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    Column {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .height(40.dp)
                .clickable { onBlogClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                Icon(imageVector = Icons.Default.DesignServices, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "관련 정보", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = Icons.Default.ArrowCircleRight,
                contentDescription = null,
            )
        }
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .height(40.dp)
                .clickable { onFavoriteClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                Icon(imageVector = Icons.Default.AccessTimeFilled, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "관심 영역", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = Icons.Default.ArrowCircleRight,
                contentDescription = null,
            )
        }
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = "오픈소스 라이센스",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onOpenSourceClick()
                },
            )
            Text(
                text = "About",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onAboutClick()
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainBottomSheetScreen() {
    MainBottomSheetScreen()
}
