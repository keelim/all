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
package com.keelim.comssa.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.keelim.comssa.data.db.entity.Search
import com.keelim.domain.comssa.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    fun favorite(favorite: Int, id: Int) = viewModelScope.launch {
        when (favorite) {
            1 -> updateFavoriteUseCase.invoke(0, id)
            0 -> updateFavoriteUseCase.invoke(1, id)
        }
    }

    fun getFavorite(): Flow<PagingData<Search>> {
        return searchUseCase.getFavorite()
            .cachedIn(viewModelScope)
    }
}
