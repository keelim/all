/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.common.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.OutputStream

private const val IMAGE_JPEG_SUFFIX = ".jpg"
private const val IMAGE_MIME_TYPE = "image/jpeg"

fun Bitmap.saveToGallery(context: Context): Uri? {
    val imageOutputStream: OutputStream

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()

            contentValues.apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "${System.currentTimeMillis()}$IMAGE_JPEG_SUFFIX"
                )
                put(MediaStore.MediaColumns.MIME_TYPE, IMAGE_MIME_TYPE)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            imageOutputStream = resolver.openOutputStream(imageUri!!)!!
            imageOutputStream.use {
                this.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()

            return imageUri
        } else {
            val imageUrl = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                this,
                "${System.currentTimeMillis()}",
                "${context.applicationInfo.loadLabel(context.packageManager)}-image"
            )
            val savedImageUri = Uri.parse(imageUrl)

            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()

            return savedImageUri
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Image not saved \n$e", Toast.LENGTH_SHORT).show()
    }
    return null
}

fun Context.getMediaUri(): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "${System.currentTimeMillis()}$IMAGE_JPEG_SUFFIX"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, IMAGE_MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    } else {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File.createTempFile(
            "${System.currentTimeMillis()}",
            IMAGE_JPEG_SUFFIX,
            directory
        )
        FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            file
        )
    }
}

fun Context.scanMediaToBitmap(uri: Uri, action: (Bitmap) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(uri.path), null) { _, _ ->
        val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(contentResolver, uri)
            )
        } else {
            val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            originalBitmap.rotateFromGalleryPreVersionP(this, uri)
        }
        action.invoke(bmp)
    }
}

fun Bitmap.rotateFromGalleryPreVersionP(context: Context, uri: Uri): Bitmap {
    val path = context.getFilePath(uri)
    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        Matrix().apply {
            postRotate(
                calculateExif(path)
            )
        },
        false
    )
}

fun Context.getFilePath(uri: Uri): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.moveToNext()

    val flag = cursor?.getColumnIndex("_data") ?: 0
    val path = cursor?.getString(flag)
    cursor?.close()

    if (path == null) {
        Toast.makeText(this, "not found file path!", Toast.LENGTH_SHORT).show()
        throw NullPointerException()
    } else {
        return path
    }
}

fun calculateExif(path: String): Float {
    val attribute = ExifInterface(path).getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    return when (attribute) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }
}
