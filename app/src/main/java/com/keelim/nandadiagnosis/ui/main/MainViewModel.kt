/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.data.network.NandaService
import com.keelim.nandadiagnosis.domain.GetAppThemeUseCase
import com.keelim.nandadiagnosis.domain.SetAppThemeUseCase
import com.squareup.okhttp.ResponseBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  getTheme: GetAppThemeUseCase,
  private val setTheme: SetAppThemeUseCase,
  private val nandaService: NandaService
) : ViewModel() {
  val theme: LiveData<Int> = getTheme.appTheme.asLiveData()

  fun setAppTheme(theme: Int) = viewModelScope.launch {
    setTheme.invoke(theme)
  }

  fun downloadDatabase(filePath: String) {
    viewModelScope.launch {
      val responseBody =
        nandaService
          .getDatabase("https://github.com/keelim/Keelim.github.io/raw/master/assets/nanda.db")
          .body()
      saveFile(responseBody, filePath)
    }
  }

  private fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String): String {
    if (body == null)
      return ""
    var input: InputStream? = null
    try {
      input = body.byteStream()
      // val file = File(getCacheDir(), "cacheFileAppeal.srl")
      val fos = FileOutputStream(pathWhereYouWantToSaveFile)
      fos.use { output ->
        val buffer = ByteArray(4 * 1024) // or other buffer size
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
          output.write(buffer, 0, read)
        }
        output.flush()
      }
      return pathWhereYouWantToSaveFile
    } catch (e: Exception) {
      Timber.e("saveFile")
    } finally {
      input?.close()
    }
    return ""
  }
}
