package com.keelim.mygrade.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.data.model.Result
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.ActivityGradeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
class GradeActivity : AppCompatActivity() {
    private val data: Result? by lazy { intent.getParcelableExtra("data") }
    private lateinit var binding: ActivityGradeBinding
    private val viewModel by viewModels<GradeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityGradeBinding>(
            this,
            R.layout.activity_grade
        ).apply {
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
        }.also {
            binding = it
        }
        initFlow()
    }

    private fun initFlow(){
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                if(state.isSave){
                    snack(binding.root, "저장이 완료되었습니다. ")
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun saveAndCopy() {
        val screenBitmap = window.decorView.rootView.drawToBitmap()
        runCatching {
            val cachePath = File(applicationContext.cacheDir, "images").apply {
                mkdirs()
            }
            val stream = FileOutputStream("$cachePath/image.png")
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            FileProvider.getUriForFile(
                applicationContext,
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
