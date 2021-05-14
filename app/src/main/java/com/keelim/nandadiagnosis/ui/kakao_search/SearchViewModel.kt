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
package com.keelim.nandadiagnosis.ui.kakao_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.kakao.search.kakaoSearchClient
import com.keelim.kakao.search.model.KakaoImageResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel : ViewModel() {
  private val kakaoSearchClient by kakaoSearchClient("a906eb8fa9271b21411c05fd6f74588a")

  private var page = 1

  val query = MutableLiveData<String>()

  private val _images = MutableLiveData<List<KakaoImageResponse>>()
  val images: LiveData<List<KakaoImageResponse>> = _images

  fun search() {
    Timber.e("search")
    viewModelScope.launch {
      val query = query.value ?: return@launch
      val response = kakaoSearchClient.searchImage(query, page = page)

      val list = mutableListOf<KakaoImageResponse>()
      list.addAll(response.documents)
      _images.value = list
    }
  }

  fun loadMore() {
    Timber.e("loadMore")
    viewModelScope.launch {
      val query = query.value ?: return@launch
      page++
      val response = kakaoSearchClient.searchImage(query, page = page)

      val list = images.value.orEmpty().toMutableList()
      list.addAll(response.documents)
      _images.value = list
    }
  }
}
