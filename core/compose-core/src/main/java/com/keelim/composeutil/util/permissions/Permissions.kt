@file:OptIn(ExperimentalPermissionsApi::class)

package com.keelim.composeutil.util.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun PermissionScreen(
    bottomSheetState: SheetState,
    multiplePermissionsState: MultiplePermissionsState,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(sheetState = bottomSheetState, onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.medium) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp),
                )
            }
            Column(
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = getTextToShowGivenPermissions(
                        multiplePermissionsState.revokedPermissions,
                        multiplePermissionsState.shouldShowRationale,
                    ),
                )
                Spacer(Modifier.height(24.dp))
                Row(Modifier.align(Alignment.End)) {
                    TextButton(onClick = onDismiss) { Text("Not now") }
                    TextButton(
                        onClick = {
                            multiplePermissionsState.launchMultiplePermissionRequest()
                        },
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}

private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean,
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""
    val textToShow = StringBuilder()

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", ")
            }

            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }

            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(
        if (shouldShowRationale) {
            "\n앱이 작동을 위해서는 권한이 필요합니다."
        } else {
            "\n앱 권한을 거절하였습니다. 원활한 앱 작동을 위해서는 권한이 필요합니다. "
        },
    )
    return textToShow.toString()
}

@Preview(showBackground = true)
@Composable
private fun PreviewPermissionScreen() {
    PermissionScreen(
        bottomSheetState = rememberModalBottomSheetState(),
        multiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
        onDismiss = {},
    )
}
