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
package com.keelim.nandadiagnosis.ui.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.model.db.DataDaoV2
import com.keelim.nandadiagnosis.model.db.NandaEntity
import kotlinx.coroutines.launch

class SearchViewModel(val database: DataDaoV2, application: Application) :
  AndroidViewModel(application) {
  private var nanda = MutableLiveData<NandaEntity?>()

  init {
    initialDB()
  }

  private fun initialDB() {
    viewModelScope.launch {
      nanda.value = getNandaDatabase()
    }
  }

  private suspend fun getNandaDatabase(): NandaEntity? {
    val nanda = database.getNanda()
    return nanda
  }

  fun getSearchMyData(keyword: String?): List<NandaEntity>? {
    val result = MutableLiveData<List<NandaEntity>>()
    viewModelScope.launch {
      val nanda_list: List<NandaEntity> = database.search(keyword)
      result.value = nanda_list
    }
    return result.value
  }

  private suspend fun getCategoryData(number: Int?): List<NandaEntity>? {
    val nanda_list: List<NandaEntity> = database.get(number)
    return nanda_list
  }
}
