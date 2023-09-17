package com.keelim.nandadiagnosis.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.OfflineBolt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.screen.category.categoryRoute
import com.keelim.nandadiagnosis.ui.screen.inappweb.webRoute
import com.keelim.setting.screen.settingsRoute
import kotlinx.collections.immutable.persistentListOf

@Stable
data class NandaNavItem(val name: String, val route: String, val icon: ImageVector)

private val nandaNavItems =
    persistentListOf(
        NandaNavItem(
            name = "Category",
            route = categoryRoute,
            icon = Icons.Rounded.Home,
        ),
        NandaNavItem(
            name = "Settings",
            route = settingsRoute,
            icon = Icons.Rounded.Settings,
        ),
        NandaNavItem(
            name = "Web",
            route = webRoute + "/nanda",
            icon = Icons.Rounded.Web,
        ),
    )

@Composable
fun NandaDrawer(
    onRouteClick: (String) -> Unit,
    onAboutClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(nandaNavItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { onRouteClick(item.route) },
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Icon(imageVector = item.icon, contentDescription = "${item.name} Icon")
                            Text(
                                modifier = Modifier.padding(start = 24.dp),
                                text = item.name,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    onAboutClick()
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Rounded.OfflineBolt,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNandaDrawer() {
    NandaDrawer(onRouteClick = {}, onAboutClick = {})
}
