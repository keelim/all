package com.keelim.mygrade.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            ))
    }

    fun changeSaveAction(isSave: Boolean) {
        _uiState.update { it.copy(isSave = isSave) }
    }
    data class GradeUiState(
        val isSave: Boolean = false,
    )
}

@AndroidEntryPoint
class GradeFragment : Fragment() {
    private val data: Result? by lazy { requireArguments().getParcelable(Keys.MAIN_TO_GRADE) }
    private val viewModel by viewModels<GradeViewModel>()

    private var _binding: FragmentGradeBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil.inflate<FragmentGradeBinding>(
        inflater,
        R.layout.fragment_grade,
        container,
        false
    ).apply{
        val (_grade, _level) = data?.grade.orEmpty() to data?.point.orEmpty()
        grade.text = _grade
        level.text = _level
        btnCopy.setOnClickListener {
            saveAndCopy()
        }
        btnSave.setOnClickListener {
            viewModel.saveSimpleHistory(_grade, _level)
            viewModel.changeSaveAction(true)
        }
        toolbar.setOnClickListener {
            findNavController().navigateUp()
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

    private fun initFlow(){
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                if(state.isSave){
                    requireActivity().snack(binding.root, "저장이 완료되었습니다. ")
                }
            }
            .flowOn(Dispatchers.Main.immediate)
            .launchIn(lifecycleScope)
    }

    private fun saveAndCopy() {
        runCatching {
            val screenBitmap = requireActivity().window.decorView.rootView.drawToBitmap()
            val cachePath = File(requireActivity().cacheDir, "images").apply { mkdirs() }
            FileOutputStream("$cachePath/image.png").use {
                screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            FileProvider.getUriForFile(
                requireContext(),
                "com.keelim.fileprovider", File(cachePath, "image.png")
            )
        }.onSuccess {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, it)
            }, "Share Capture Image"))
        }.onFailure {
            it.printStackTrace()
        }
    }
}
