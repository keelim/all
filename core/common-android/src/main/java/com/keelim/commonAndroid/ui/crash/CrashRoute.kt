package com.keelim.commonAndroid.ui.crash

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space64
import com.keelim.composeutil.resource.space8

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
    val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
    val androidVersion =
        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val errorMsg =
        "Version: $appVersion\nDevice: $deviceModel\nSystem: $androidVersion\n\nStack trace: \n\n$errorMessage\n\n\"현재 에러가 발생했습니다. 앱을 재시작해주시기 바랍니다."

    CrashScreen(
        text = errorMsg,
        onAppRefresh = onAppRefresh
    )
}

@Composable
fun CrashScreen(
    text: String,
    onAppRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationBackArrowBar(
            "에러 확인 중"
        )
        Text(
            text = text,
            modifier = Modifier.padding(space8)
        )
        Spacer(
            modifier = Modifier.height(space64)
        )
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            modifier = Modifier.clickable { onAppRefresh() }
                .align(Alignment.CenterHorizontally)
        )
    }
}
x
@Preview
@Composable
private fun PreviewCrashScreen() {
    CrashScreen(
        text = "현재 에러가 발생했습니다. 앱을 재시작해주시기 바랍니다.",
        onAppRefresh = {},
    )
}

