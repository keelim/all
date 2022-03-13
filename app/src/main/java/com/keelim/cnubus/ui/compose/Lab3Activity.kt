package com.keelim.cnubus.ui.compose

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.keelim.cnubus.data.model.File
import com.keelim.cnubus.worker.FileDownloadWorker
import com.keelim.common.extensions.toast
import com.keelim.compose.ui.setThemeContent

class Lab3Activity : ComponentActivity() {
    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>
    private val workManager by lazy { WorkManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionMap ->
            var isGranted = false
            permissionMap.forEach { (s, b) ->
                isGranted = b
            }

            if (!isGranted) {
                toast("Permission Not Granted")
            }
        }

        setThemeContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                requestMultiplePermission.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                Home()
            }
        }
    }

    private fun startDownloadingFile(
        file: File,
        success: (String) -> Unit,
        failed: (String) -> Unit,
        running: () -> Unit,
    ) {
        val data = Data.Builder()
            .putString(FileDownloadWorker.KEY_FILE_NAME, file.name)
            .putString(FileDownloadWorker.KEY_FILE_URL, file.url)
            .putString(FileDownloadWorker.KEY_FILE_TYPE, file.type)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            "oneFileDownloadWork_${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            fileDownloadWorker
        )

        workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
            .observe(this) { info ->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            success(it.outputData.getString(FileDownloadWorker.KEY_FILE_URI) ?: "")
                        }
                        WorkInfo.State.FAILED -> {
                            failed("Downloading failed!")
                        }
                        WorkInfo.State.RUNNING -> {
                            running()
                        }
                        else -> {
                            failed("Something went wrong")
                        }
                    }
                }
            }
    }

    @Composable
    fun Home() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val data = remember {
                mutableStateOf(
                    File(
                        id = "10",
                        name = "Pdf File 10 MB",
                        type = "PDF",
                        url = "https://www.learningcontainer.com/wp-content/uploads/2019/09/sample-pdf-download-10-mb.pdf",
                        downloadedUri = null
                    )
                )
            }
            ItemFile(
                file = data.value,
                startDownload = {
                    startDownloadingFile(
                        file = data.value,
                        success = {
                            data.value = data.value.copy().apply {
                                isDownloading = false
                                downloadedUri = it
                            }
                        },
                        failed = {

                            data.value = data.value.copy().apply {
                                isDownloading = false
                                downloadedUri = null
                            }
                        },
                        running = {
                            data.value = data.value.copy().apply {
                                isDownloading = true
                            }
                        }
                    )
                },
                openFile = {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(it.downloadedUri?.toUri(), "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    })
                }
            )
        }
    }
}
