package com.keelim.common.platform.export

data class ExportDocument(
    val fileName: String,
    val mimeType: String,
    val content: ByteArray,
)

data class ExportedFile(
    val reference: String,
    val mimeType: String,
)

interface ExportFileWriter {
    suspend fun write(document: ExportDocument): ExportedFile
}

interface ExportShareLauncher {
    fun share(file: ExportedFile)
}

object ExportDocumentPolicy {
    private val fileName = Regex("[\\p{L}\\p{N}][\\p{L}\\p{N}._ -]{0,119}")
    private val mimeType = Regex("[a-zA-Z0-9.+-]+/[a-zA-Z0-9.+-]+")

    fun violations(document: ExportDocument): List<String> = buildList {
        if (!fileName.matches(document.fileName) || ".." in document.fileName) {
            add("invalid export file name")
        }
        if (!mimeType.matches(document.mimeType)) add("invalid MIME type")
        if (document.content.isEmpty()) add("export content must not be empty")
    }
}
