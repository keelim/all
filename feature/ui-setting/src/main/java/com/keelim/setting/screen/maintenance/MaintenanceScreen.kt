package com.keelim.setting.screen.maintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MaintenanceRoute(
    viewModel: MaintenanceViewModel = hiltViewModel(),
) {
    val isUnderMaintenance by viewModel.isUnderMaintenance.collectAsStateWithLifecycle()
    if (isUnderMaintenance) {
        MaintenanceScreen()
    }
}

@Composable
fun MaintenanceScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        KuiIcon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = KuiTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        KuiText(
            text = "Under Maintenance",
            style = KuiTheme.typography.headlineSmall,
            color = KuiTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        KuiText(
            text = "We are currently performing maintenance.\nPlease check back later.",
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
