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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.data.db.DataDao
import com.keelim.nandadiagnosis.data.db.NandaEntity
import kotlinx.coroutines.launch

class SearchViewModel(
  val database: DataDao,
  application: Application
) : AndroidViewModel(application) {
  private var _items = MutableLiveData<List<NandaEntity>>()
  val items: LiveData<List<NandaEntity>> = _items

  fun search(word: String) {
    viewModelScope.launch {
      _items.value = getItemFromDatabase(word)
    }
  }

  private suspend fun getItemFromDatabase(word: String): List<NandaEntity> {
    return database.search(word)
  }
}
