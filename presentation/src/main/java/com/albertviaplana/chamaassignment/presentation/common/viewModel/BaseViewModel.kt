package com.albertviaplana.chamaassignment.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
open class BaseViewModel<T> : ViewModel() {
    protected val _state = MutableStateFlow<T?>(null)
    val state: Flow<T?> = _state

}