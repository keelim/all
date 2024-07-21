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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.util.trace
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.NandaRoute
import com.keelim.nandadiagnosis.R
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface NandaNavItem {
    val name: String
    val route: AppRoute
    val icon: ImageVector

    data class BigType(
        override val name: String,
        override val route: AppRoute,
        override val icon: ImageVector,
    ) : NandaNavItem

    data class SmallType(
        override val name: String,
        override val route: AppRoute,
        override val icon: ImageVector,
    ) : NandaNavItem
}

private val nandaNavItems =
    persistentListOf(
        NandaNavItem.BigType(
            name = "Category",
            route = NandaRoute.Category,
            icon = Icons.Rounded.Home,
        ),
        NandaNavItem.BigType(
            name = "Web",
            route = NandaRoute.Web(uri = "nanda"),
            icon = Icons.Rounded.Info,
        ),
        NandaNavItem.BigType(
            name = "Nutrient",
            route = NandaRoute.Nutrient,
            icon = Icons.Rounded.Person,
        ),
        NandaNavItem.SmallType(
            name = "NutrientTimer",
            route = NandaRoute.NutrientTimer,
            icon = Icons.Rounded.DateRange,
        ),
        NandaNavItem.BigType(
            name = "Settings",
            route = FeatureRoute.Settings,
            icon = Icons.Rounded.Settings,
        ),
    )

@Composable
fun NandaDrawer(
    onRouteClick: (AppRoute) -> Unit,
    onAboutClick: () -> Unit,
) = trace("NandaDrawer") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space16, vertical = space24),
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Spacer(modifier = Modifier.height(space8))
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(start = space8),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(space24))
            LazyColumn {
                items(nandaNavItems) { item ->
                    NavigationCard(item, onRouteClick)
                    Spacer(modifier = Modifier.height(space8))
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
                imageVector = Icons.Rounded.Settings,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@Composable
private fun NavigationCard(
    item: NandaNavItem,
    onRouteClick: (AppRoute) -> Unit,
) = trace("NavigationCard") {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space4),
            shape = RoundedCornerShape(space12),
            onClick = { onRouteClick(item.route) },
        ) {
            Row(
                modifier = Modifier.padding(horizontal = space16, vertical = space8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                when (item) {
                    is NandaNavItem.SmallType -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                            contentDescription = "${item.name} Icon",
                        )
                        Spacer(
                            modifier = Modifier.width(space12),
                        )
                    }

                    else -> Unit
                }
                Icon(imageVector = item.icon, contentDescription = "${item.name} Icon")
                Text(
                    modifier = Modifier.padding(start = space24),
                    text = item.name,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNandaDrawer() {
    NandaDrawer(onRouteClick = {}, onAboutClick = {})
}
