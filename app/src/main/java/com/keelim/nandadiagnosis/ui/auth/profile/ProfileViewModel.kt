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
package com.keelim.nandadiagnosis.ui.auth.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.base.BaseViewModel
import com.keelim.nandadiagnosis.di.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
  private val preferenceManager: PreferenceManager,
) : BaseViewModel() {
  private var _profileState = MutableLiveData<ProfileState>(ProfileState.UnInitialized)
  val profileState: LiveData<ProfileState> get() = _profileState

  override fun fetchData(): Job = viewModelScope.launch {
    setState(ProfileState.Loading)
    preferenceManager.getIdToken()?.let {
      setState(ProfileState.Login(it))
    } ?: kotlin.run {
      setState(ProfileState.Success.NotRegistered)
    }
  }

  private fun setState(state: ProfileState) {
    _profileState.postValue(state)
  }
}