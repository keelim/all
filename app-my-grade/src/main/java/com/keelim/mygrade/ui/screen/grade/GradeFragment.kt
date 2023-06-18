package com.keelim.mygrade.ui.screen.grade

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.os.BuildCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import com.google.modernstorage.photopicker.PhotoPicker
import com.google.modernstorage.storage.AndroidFileSystem
import com.keelim.common.extensions.toast
import com.keelim.composeutil.setThemeContent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream


@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class GradeFragment : Fragment() {
    private val viewModel: GradeViewModel by viewModels()

    private val photoPicker =
        registerForActivityResult(
            PhotoPicker(),
        ) { uris ->
            startActivity(
                Intent.createChooser(
                    Intent().apply {
                        action = Intent.ACTION_SEND_MULTIPLE
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                        type = "image/*"
                    },
                    "선택 이미지 공유",
                ),
            )
            viewModel.updateMessage()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setThemeContent {
        GradeRoute(
            onCopyClick = ::saveAndCopy,
            onShareClick = ::share
        )
    }

    private fun saveAndCopy() {
        requestAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ_AND_WRITE,
                types =
                listOf(
                    StoragePermissions.FileType.Image,
                    StoragePermissions.FileType.Document,
                ),
                createdBy = StoragePermissions.CreatedBy.Self,
            ),
        )
    }

    private fun share() {
        photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
    }

    private fun saveAndCopyInternal() {
        runCatching {
            val screenBitmap = requireActivity().window.decorView.rootView.drawToBitmap()
            val fileSystem = AndroidFileSystem(requireContext())

            val cachePath = File(requireActivity().cacheDir, "images").apply { mkdirs() }
            FileOutputStream("$cachePath/image.png").use {
                screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            FileProvider.getUriForFile(
                requireContext(),
                "com.keelim.fileprovider",
                File(cachePath, "image.png"),
            )
        }
            .onSuccess { photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1)) }
            .onFailure { it.printStackTrace() }
    }


    private val requestAccess =
        registerForActivityResult(RequestAccess()) { hasAccess ->
            takeIf { hasAccess }
                ?.run {
                    saveAndCopyInternal()
                } ?: run {
                toast("권한이 필요합니다. 다시 한번 시도해주세요")
            }
        }
}
