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
package com.keelim.comssa.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.domain.SearchUseCase
import com.keelim.comssa.domain.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
) : ViewModel() {
    val downloadLink:MutableStateFlow<String> = MutableStateFlow("")
    fun favorite(favorite: Int, id: Int) = viewModelScope.launch {
        when (favorite) {
            0 -> updateFavoriteUseCase.invoke(1, id)
            1 -> updateFavoriteUseCase.invoke(0, id)
        }
    }

    fun getContent(query:String = ""): Flow<PagingData<Search>> {
        return searchUseCase.getContent(query)
            .cachedIn(viewModelScope)
    }

    fun getDownloadLink(password:String) = viewModelScope.launch{
        runCatching {
            searchUseCase.getDownloadLink(password)
        }.onSuccess {
            if(it.flag){
                downloadLink.emit(it.password)
            }
        }
    }
}
