package com.keelim.labs.capture

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.labs.databinding.ActivityCaptureBinding
import com.keelim.common.extensions.getMediaUri
import com.keelim.common.extensions.saveToGallery
import com.keelim.common.extensions.scanMediaToBitmap

class CaptureActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        buttonSaveBitmap.setOnClickListener {
            val bitmapDrawable = imageViewSample.drawable as? BitmapDrawable
            val bitmap = bitmapDrawable?.bitmap
            bitmap?.saveToGallery(this@CaptureActivity)
        }
        buttonTakePhotoSaveToGallery.setOnClickListener {
            photoUri = this@CaptureActivity.getMediaUri()
            takePhoto.launch(photoUri)
        }
        buttonGetImageFromGallery.setOnClickListener {
            choosePhoto.launch("image/Pictures/*")
        }
    }
}