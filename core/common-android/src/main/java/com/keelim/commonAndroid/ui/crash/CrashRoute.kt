package com.keelim.commonAndroid.ui.crash

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

private val crashSpace8 = 8.dp
private val crashSpace12 = 12.dp
private val crashSpace16 = 16.dp
private val crashSpace24 = 24.dp

@Composable
fun CrashRoute(
    errorMessage: String,
    onAppRefresh: () -> Unit,
    viewModel: CrashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val appVersion = context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName ?: "Unknown"
    val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
    val androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    CrashScreen(
        appVersion = appVersion,
        deviceModel = deviceModel,
        androidVersion = androidVersion,
        stackTrace = errorMessage,
        onAppRefresh = onAppRefresh,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrashScreen(
    appVersion: String,
    deviceModel: String,
    androidVersion: String,
    stackTrace: String,
    onAppRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "오류가 발생했습니다",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = crashSpace16),
            verticalArrangement = Arrangement.spacedBy(crashSpace16),
        ) {
            item {
                Spacer(modifier = Modifier.height(crashSpace8))
            }

            // Error Header Section
            item {
                ErrorHeaderSection()
            }

            // Device Info Card
            item {
                DeviceInfoCard(
                    appVersion = appVersion,
                    deviceModel = deviceModel,
                    androidVersion = androidVersion,
                )
            }

            // Stack Trace Card
            item {
                StackTraceCard(stackTrace = stackTrace)
            }

            // Restart Button
            item {
                Button(
                    onClick = onAppRefresh,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(crashSpace12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(crashSpace8))
                    Text(
                        text = "앱 다시 시작하기",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(crashSpace24))
            }
        }
    }
}

@Composable
private fun ErrorHeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(40.dp),
            color = MaterialTheme.colorScheme.errorContainer,
        ) {
            Icon(
                imageVector = BugIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(crashSpace16),
                tint = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
        Spacer(modifier = Modifier.height(crashSpace16))
        Text(
            text = "예기치 않은 오류가 발생했습니다",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(crashSpace8))
        Text(
            text = "앱을 다시 시작해 주세요.\n문제가 지속되면 개발팀에 문의해 주세요.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DeviceInfoCard(
    appVersion: String,
    deviceModel: String,
    androidVersion: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(crashSpace12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(crashSpace16),
            verticalArrangement = Arrangement.spacedBy(crashSpace8),
        ) {
            Text(
                text = "기기 정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(crashSpace8))
            DeviceInfoRow(label = "앱 버전", value = appVersion)
            DeviceInfoRow(label = "기기 모델", value = deviceModel)
            DeviceInfoRow(label = "OS 버전", value = androidVersion)
        }
    }
}

@Composable
private fun DeviceInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun StackTraceCard(stackTrace: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(crashSpace12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
    ) {
        Column(
            modifier = Modifier.padding(crashSpace16),
        ) {
            Text(
                text = "오류 상세 정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(crashSpace12))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(crashSpace8),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Text(
                    text = stackTrace,
                    modifier = Modifier.padding(crashSpace12),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCrashScreen() {
    CrashScreen(
        appVersion = "1.0.0",
        deviceModel = "Samsung SM-S911N",
        androidVersion = "Android 14 (API 34)",
        stackTrace = "java.lang.NullPointerException: Attempt to invoke virtual method...\n    at com.example.app.MainActivity.onCreate(MainActivity.kt:42)\n    at android.app.Activity.performCreate(Activity.java:8051)",
        onAppRefresh = {},
    )
}
