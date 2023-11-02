package com.keelim.commonAndroid.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

interface State
interface UserAction

abstract class KeelimViewModel<state : State, userAction : UserAction>(
    startWith: state,
) : ViewModel() {

    private val innerState = MutableStateFlow(startWith)
    val state: StateFlow<state> = innerState.asStateFlow()

    private val internalUserActions = MutableSharedFlow<userAction>(
        extraBufferCapacity = 1
    )
    private val userActions: Flow<userAction> = internalUserActions.asSharedFlow()

    init {
        viewModelScope.launch {
            userActions.collect {
                processUserAction(it)
            }
        }
    }

    protected abstract suspend fun processUserAction(userAction: userAction)

    protected suspend fun emitState(state: state) {
        innerState.value = state
        withContext(Dispatchers.Main.immediate) {
            Timber.d("emitState ${this@KeelimViewModel.javaClass.simpleName} ${state.javaClass.simpleName}")
            innerState.emit(state)
        }
    }

    fun emitUserAction(userAction: userAction) {
        viewModelScope.launch {
            withContext(Dispatchers.Main.immediate) {
                Timber.d("emitUserAction${this@KeelimViewModel.javaClass.simpleName} ➡️ ${userAction.javaClass.simpleName}")
                internalUserActions.emit(userAction)
            }
        }
    }
}
