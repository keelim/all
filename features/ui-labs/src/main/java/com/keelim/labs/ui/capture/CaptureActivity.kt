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
package com.keelim.labs.ui.capture

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.keelim.common.extensions.getMediaUri
import com.keelim.common.extensions.saveToGallery
import com.keelim.common.extensions.scanMediaToBitmap
import com.keelim.labs.R
import com.keelim.labs.databinding.ActivityCaptureBinding
import com.keelim.labs.services.LabBoundService
import com.keelim.labs.services.LabBoundService2
import java.io.FileOutputStream

class CaptureActivity : AppCompatActivity(R.layout.activity_capture) {

    private val binding by lazy { ActivityCaptureBinding.inflate(layoutInflater) }

    private val choosePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imageViewImageFromGallery.setImageURI(it)
    }
    private var photoUri: Uri? = null
    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && photoUri != null) {
                scanMediaToBitmap(photoUri!!) {
                    runOnUiThread {
                        binding.imageViewImageFromGallery.setImageURI(photoUri!!)
                    }
                }
            }
        }
    private var labBoundService: LabBoundService? = null
    private var isBound = false

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                result.data?.let {
                }
            }
        }
    }

    /*private val takePhotoFromAlbumIntent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    val takePhotoFromCameraLauncher = // ???????????? ?????? ????????? ????????????
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { takenPhoto ->
            if (takenPhoto != null) {
                onPhotoChanged(takenPhoto)
            } else {
                toast(context, StringAsset.Toast.ErrorTakenPhoto)
            }
        }

    val takePhotoFromAlbumLauncher = // ??????????????? ?????? ????????????
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    onPhotoChanged(uri.parseBitmap(context))
                } ?: run {
                    toast(context, StringAsset.Toast.ErrorTakenPhoto)
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                toast(context, StringAsset.Toast.ErrorTakenPhoto)
            }
        }
        */
     */

    private val myConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
                val binder = service as LabBoundService.LocalBinder
                labBoundService = binder.getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isBound = false
            }
        }
    }

    private lateinit var myService: Messenger
    private var isBound2 = false

    private val myConnection2 by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                if (::myService.isInitialized.not()) {
                    myService = Messenger(p1)
                }
                isBound2 = true
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isBound2 = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initServices()
        showTime()
        sendMessage()
    }

    private fun initViews() = with(binding) {
        buttonSaveBitmap.setOnClickListener {
            val bitmapDrawable = imageViewSample.drawable as? BitmapDrawable
            val bitmap = bitmapDrawable?.bitmap
            bitmap?.saveToGallery(this@CaptureActivity)
            sendMessage()
        }
        buttonTakePhotoSaveToGallery.setOnClickListener {
            photoUri = this@CaptureActivity.getMediaUri()
            takePhoto.launch(photoUri)
        }
        buttonGetImageFromGallery.setOnClickListener {
            choosePhoto.launch("image/Pictures/*")
        }
        tvStorageAccess.setOnClickListener {
            launcher.launch(
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TITLE, "newfile.txt")
                }
            )
        }
    }

    private fun initServices() {
        bindService(
            Intent(this, LabBoundService::class.java),
            myConnection,
            Context.BIND_AUTO_CREATE
        )
        bindService(
            Intent(this, LabBoundService2::class.java),
            myConnection2,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun showTime() {
        val value = labBoundService?.getCurrentTime()
        binding.tvTime.text = value
    }

    private fun sendMessage() {
        if (isBound2.not()) return

        val msg = Message.obtain()
        val bundle = bundleOf(
            "MyString" to "Message Receiver"
        )
        msg.data = bundle
        runCatching {
            myService.send(msg)
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun writeFileContent(uri: Uri) {
        runCatching {
            val pfd = contentResolver.openFileDescriptor(uri, "w")
            val fileOutputStream = FileOutputStream(
                pfd?.fileDescriptor
            )
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
            pfd?.close()
        }.onFailure {
        }
    }
}
