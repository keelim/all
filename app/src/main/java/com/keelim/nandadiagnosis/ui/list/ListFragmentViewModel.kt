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
package com.keelim.nandadiagnosis.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.base.BaseViewModel
import com.keelim.nandadiagnosis.usecase.GetNandaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListFragmentViewModel
@Inject
constructor(
  private val getNandaListUseCase: GetNandaListUseCase,
) : BaseViewModel() {
  private var _nandaListState = MutableLiveData<NandaListState>(NandaListState.UnInitialized)
  val nandaListState: LiveData<NandaListState> get() = _nandaListState

  override fun fetchData(): Job = viewModelScope.launch {
    setState(
      NandaListState.Loading
    )
    setState(
      NandaListState.Success(
        getNandaListUseCase.invoke()
      )
    )
  }

  private fun setState(state: NandaListState) {
    _nandaListState.postValue(state)
  }
}
