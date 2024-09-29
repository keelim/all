package com.keelim.composeutil.util.permission

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SimpleAcquirePermissions(
    permissions: List<String>,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_CREATE,
    onGrant: () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)
    LifecycleEventEffect(lifecycleEvent) {
        if(permissionsState.allPermissionsGranted) {
            onGrant()
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
