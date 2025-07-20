package com.keelim.comssa.ui.screen.main.finance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.keelim.composeutil.component.fab.FabButtonItem

// FAB 버튼 아이템들
data class FilterAll(
    override val imageVector: ImageVector = Icons.Filled.List,
    override val label: String = "전체",
) : FabButtonItem

data class FilterStock(
    override val imageVector: ImageVector = Icons.Filled.Star,
    override val label: String = "주식",
) : FabButtonItem

data class FilterCrypto(
    override val imageVector: ImageVector = Icons.Filled.Favorite,
    override val label: String = "암호화폐",
) : FabButtonItem

data class FilterForex(
    override val imageVector: ImageVector = Icons.Filled.Settings,
    override val label: String = "외환",
) : FabButtonItem

data class FilterEconomy(
    override val imageVector: ImageVector = Icons.Filled.Info,
    override val label: String = "경제",
) : FabButtonItem

data class FilterRealEstate(
    override val imageVector: ImageVector = Icons.Filled.Home,
    override val label: String = "부동산",
) : FabButtonItem
