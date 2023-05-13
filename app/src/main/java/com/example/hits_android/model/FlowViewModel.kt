package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

var _output = MutableStateFlow("")
class FlowViewModel : ViewModel() {
    val output: StateFlow<String> = _output.asStateFlow()

    fun setCurrentValue(newValue: String) {
        _output.value += newValue
        println(_output.value)
    }
}