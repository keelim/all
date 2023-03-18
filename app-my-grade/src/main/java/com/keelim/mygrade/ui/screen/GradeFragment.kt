package com.keelim.mygrade.ui.screen

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.os.BuildCompat
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import com.google.modernstorage.photopicker.PhotoPicker
import com.google.modernstorage.storage.AndroidFileSystem
import com.keelim.common.extensions.snack
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.data.model.Result
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.FragmentGradeBinding
import com.keelim.mygrade.utils.Keys
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GradeUiState())
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    fun saveSimpleHistory(grade: String, rank: String) = viewModelScope.launch {
        ioRepository.insertSimpleHistories(
            SimpleHistory(
                name = "",
                grade = grade,
                rank = rank
            )
        )
        changeSaveAction(isSave = true)
    }

    private fun changeSaveAction(isSave: Boolean) {
        _uiState.update { it.copy(isSave = isSave) }
    }

    data class GradeUiState(
        val isSave: Boolean = false,
    )
}

@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class GradeFragment : Fragment() {
    private val viewModel by viewModels<GradeViewModel>()
    private val data: Result? by lazy {
        requireArguments().getParcelable(
            Keys.MAIN_TO_GRADE
        )
    }

    private var _binding: FragmentGradeBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val photoPicker = registerForActivityResult(
        PhotoPicker()
    ) { uris ->
        startActivity(
            Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND_MULTIPLE
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                    type = "image/*"
                },
                "선택 이미지 공유"
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DataBindingUtil.inflate<FragmentGradeBinding>(
        inflater,
        R.layout.fragment_grade,
        container,
        false
    ).apply {
        val (_grade, _level) = data?.grade.orEmpty() to data?.point.orEmpty()
        grade.text = _grade
        level.text = _level
        btnCopy.setOnClickListener {
            saveAndCopy()
        }
        btnSave.setOnClickListener {
            viewModel.saveSimpleHistory(_grade, _level)
        }
        imageviewBack.setOnClickListener {
            findNavController().navigateUp()
        }
        btnShare.setOnClickListener {
            photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
        }
    }.also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFlow() {
        SdkExtensions
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collectLatest { uiState ->
                        if (uiState.isSave) {
                            requireActivity().snack(binding.root, "저장이 완료되었습니다. ")
                        }
                    }
            }
        }
    }

    private val requestAccess = registerForActivityResult(RequestAccess()) { hasAccess ->
        if (hasAccess) {
            saveAndCopyInternal()
        } else {
            requireActivity().snack(binding.root, "권한이 필요합니다. 다시 한번 시도해주세요")
        }
    }

    private fun saveAndCopy() {
        requestAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ_AND_WRITE,
                types = listOf(
                    StoragePermissions.FileType.Image,
                    StoragePermissions.FileType.Document
                ),
                createdBy = StoragePermissions.CreatedBy.Self
            )
        )
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
                "com.keelim.fileprovider", File(cachePath, "image.png")
            )
        }.onSuccess {
            photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
        }.onFailure {
            it.printStackTrace()
        }
    }
}
