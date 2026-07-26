package com.keelim.commonAndroid.platform.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.keelim.common.platform.export.ExportDocument
import com.keelim.common.platform.export.ExportDocumentPolicy
import com.keelim.common.platform.export.ExportFileWriter
import com.keelim.common.platform.export.ExportShareLauncher
import com.keelim.common.platform.export.ExportedFile
import java.io.File
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidExportFileWriter(
    private val context: Context,
    private val now: () -> Instant = Instant::now,
) : ExportFileWriter {
    override suspend fun write(document: ExportDocument): ExportedFile = withContext(Dispatchers.IO) {
        val violations = ExportDocumentPolicy.violations(document)
        require(violations.isEmpty()) { violations.joinToString() }
        val directory = File(context.cacheDir, EXPORT_DIRECTORY).apply { mkdirs() }
        cleanExpired(directory)
        val file = File(directory, document.fileName)
        file.writeBytes(document.content)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.keelim-platform-files",
            file,
        )
        ExportedFile(uri.toString(), document.mimeType)
    }

    private fun cleanExpired(directory: File) {
        val cutoff = now().minus(Duration.ofDays(EXPIRY_DAYS)).toEpochMilli()
        directory.listFiles()?.filter { it.lastModified() < cutoff }?.forEach(File::delete)
    }
}

class AndroidExportShareLauncher(
    private val context: Context,
) : ExportShareLauncher {
    override fun share(file: ExportedFile) {
        val share = Intent(Intent.ACTION_SEND)
            .setType(file.mimeType)
            .putExtra(Intent.EXTRA_STREAM, file.reference.toUri())
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(
            Intent.createChooser(share, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}

private const val EXPORT_DIRECTORY = "keelim_platform_exports"
private const val EXPIRY_DAYS = 7L
