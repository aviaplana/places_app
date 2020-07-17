package com.albertviaplana.chamaassignment.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
abstract class BaseViewModel<S, E> : ViewModel() {
    protected abstract val _state: MutableStateFlow<S>
    val state: StateFlow<S> by lazy { _state }

    protected var currentState: S
        get() = _state.value
        set(nextState) {
            _state.value = nextState
        }

    private val _event = BroadcastChannel<E>(1)
    val event: ReceiveChannel<E> = _event.openSubscription()


    fun sendEventViewModelScope(e: E) {
        viewModelScope.launch {
            _event.send(e)
        }
    }
}