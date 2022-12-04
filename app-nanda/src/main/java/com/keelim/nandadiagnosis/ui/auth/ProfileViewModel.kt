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
package com.keelim.nandadiagnosis.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keelim.data.di.IoDispatcher
import com.keelim.data.di.PreferenceManager
import com.keelim.nandadiagnosis.domain.favorite.GetFavoriteListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _profileState = MutableLiveData<ProfileState>(ProfileState.UnInitialized)
    val profileState: LiveData<ProfileState> get() = _profileState

    fun fetchData() = viewModelScope.launch {
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

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(dispatcher) {
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUser(user: FirebaseUser?) = viewModelScope.launch {
        user?.let { user ->
            Timber.d("유저 값 $user")
            setState(
                ProfileState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl,
                    getFavoriteListUseCase()
                )
            )
        } ?: kotlin.run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(dispatcher) {
            preferenceManager.removedToken()
        }
        fetchData()
    }
}
