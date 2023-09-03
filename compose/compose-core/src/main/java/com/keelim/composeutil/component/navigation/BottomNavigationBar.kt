package com.keelim.composeutil.component.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        content = {
            AppNavItem(
                imageVector = Icons.Default.Home,
                selected = true,
                onClick = {}
            )
            AppNavItem(
                imageVector = Icons.Default.CheckCircle,
                selected = false,
                onClick = {}
            )
            AppNavItem(
                imageVector = Icons.Default.Face,
                selected = false,
                onClick = {}
            )
            AppNavItem(
                imageVector = Icons.Default.Settings,
                selected = false,
                onClick = {}
            )
        }
    )
}

@Composable
fun RowScope.AppNavItem(
    imageVector: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null
            )
        },
        onClick = onClick,
        selected = selected,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            unselectedIconColor = MaterialTheme.colorScheme.secondary,
            indicatorColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAppBottomNavigationBar() {
    AppBottomNavigationBar()
}
