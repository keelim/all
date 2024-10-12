package com.keelim.commonAndroid.ui.crash

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun CrashRoute(
    errorMessage: String,
    viewModel: CrashViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val appVersion = context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName
    val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
    val androidVersion =
        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val errorReport =
        "Version: $appVersion\nDevice: $deviceModel\nSystem: $androidVersion\n\nStack trace: \n\n$errorMessage"
}

@Composable
fun CrashScreen() {

}

@Preview
@Composable
private fun PreviewCrashScreen() {
    CrashScreen()
}

@HiltViewModel
class CrashViewModel @Inject constructor(

) : ViewModel() {

}
