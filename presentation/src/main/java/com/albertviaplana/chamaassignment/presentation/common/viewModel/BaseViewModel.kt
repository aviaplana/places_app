package com.albertviaplana.chamaassignment.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot

@ExperimentalCoroutinesApi
abstract class BaseViewModel<T> : ViewModel() {
    abstract val _state: MutableStateFlow<T>
    val state: Flow<T> by lazy { _state }

}