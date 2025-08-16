package com.keelim.commonAndroid.ui.crash

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.commonAndroid.R
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space8

/**
 * Composable route for displaying crash reports and error information
 * Follows MVVM pattern with Hilt dependency injection
 *
 * @param errorMessage The error message to display
 * @param onAppRefresh Callback for refreshing/restarting the app
 * @param viewModel ViewModel for crash reporting functionality
 */
@Composable
fun CrashRoute(
    errorMessage: String,
    onAppRefresh: () -> Unit,
    viewModel: CrashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val appVersion = context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName
    
    val deviceModel = stringResource(
        id = R.string.crash_report_device_format,
        Build.MANUFACTURER,
        Build.MODEL
    )
    
    val androidVersion = stringResource(
        id = R.string.crash_report_android_version_format,
        Build.VERSION.RELEASE,
        Build.VERSION.SDK_INT
    )
    
    val crashMessage = stringResource(id = R.string.crash_report_message)

    val errorMsg = stringResource(
        id = R.string.crash_report_version_format,
        appVersion ?: "",
        deviceModel,
        androidVersion,
        errorMessage,
        crashMessage
    )

    CrashScreen(
        text = errorMsg,
        onAppRefresh = onAppRefresh,
    )
}

/**
 * Internal composable for displaying crash screen UI
 * 
 * @param text The formatted error message to display
 * @param onAppRefresh Callback for refreshing/restarting the app
 */
@Composable
fun CrashScreen(
    text: String,
    onAppRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavigationBackArrowBar(
            title = stringResource(id = R.string.crash_report_title),
        )
        LazyColumn {
            item {
                Text(
                    text = text,
                    modifier = Modifier.padding(space8),
                )
            }
            item {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.crash_report_refresh_description),
                    modifier = Modifier
                        .clickable { onAppRefresh() },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCrashScreen() {
    CrashScreen(
        text = "Sample error message for preview",
        onAppRefresh = {},
    )
}
