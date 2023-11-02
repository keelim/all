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
interface Event
interface UserAction

abstract class AbstractViewModel<state : State, event : Event, userAction : UserAction>(
    startWith: state,
) : ViewModel() {

    private val innerState = MutableStateFlow(startWith)
    val state: StateFlow<state> = innerState.asStateFlow()

    private val internalEvents = Channel<event>(Channel.BUFFERED)
    val events = internalEvents.receiveAsFlow()

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

    abstract suspend fun processUserAction(userAction: UserAction)

    protected suspend fun emitState(state: state) {
        innerState.value = state
        withContext(Dispatchers.Main.immediate) {
            Timber.d("emitState ${this@AbstractViewModel.javaClass.simpleName} ${state.javaClass.simpleName}")
            innerState.emit(state)
        }
    }

    protected suspend fun emitEvent(event: event) {
        withContext(Dispatchers.Main.immediate) {
            Timber.d("emitEvent ${this@AbstractViewModel.javaClass.simpleName} ${event.javaClass.simpleName}")
            internalEvents.trySend(event)
        }
    }

    fun emitUserAction(userAction: userAction) {
        viewModelScope.launch {
            withContext(Dispatchers.Main.immediate) {
                Timber.d("emitUserAction${this@AbstractViewModel.javaClass.simpleName} ➡️ ${userAction.javaClass.simpleName}")
                internalUserActions.emit(userAction)
            }
        }
    }
}
