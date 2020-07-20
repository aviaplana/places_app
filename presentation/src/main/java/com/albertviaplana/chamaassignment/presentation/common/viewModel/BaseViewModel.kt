package com.albertviaplana.chamaassignment.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
abstract class BaseViewModel<S, E: Any>(private val coroutineScopeProvider: CoroutineScope? = null) : ViewModel() {
    protected val coroutineScope = getViewModelScope(coroutineScopeProvider)

    protected abstract val _state: MutableStateFlow<S>
    val state: StateFlow<S> by lazy { _state }

    protected var currentState: S
        get() = _state.value
        set(nextState) {
            println("STATE: $nextState")
            _state.value = nextState
        }

    private val _event = BroadcastChannel<E>(1)
    val event: ReceiveChannel<E>
        get() = _event.openSubscription()

    protected fun E.sendScoped() {
        println("EVENT: ${javaClass.simpleName}")
        coroutineScope.launch {
            _event.send(this@sendScoped)
        }
    }

    private fun getViewModelScope(coroutineScope: CoroutineScope?) =
        coroutineScope ?: this.viewModelScope
}