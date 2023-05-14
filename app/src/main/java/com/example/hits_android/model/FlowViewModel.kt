package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import com.example.hits_android.TestProgram
import com.example.hits_android.blocks.blockIndex
import com.example.hits_android.blocks.blockList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

var _output = MutableStateFlow("")

class FlowViewModel : ViewModel() {
    val output: StateFlow<String> = _output.asStateFlow()

    fun setCurrentValue(newValue: String) {
        _output.value += newValue
    }

    fun setError(error: String){
        _output.value = error
    }
    private val _isProgramRunning = MutableStateFlow(false)
    val isProgramRunning: StateFlow<Boolean> = _isProgramRunning.asStateFlow()

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun startProgram() {
        if (!isProgramRunning.value) {
            job = GlobalScope.launch {
                TestProgram()
                try{
                    while (blockIndex < blockList.size) {
                        blockList[blockIndex].runCodeBlock()
                    }
                    _isProgramRunning.value = false
                } catch (e: java.lang.Exception){
                     setError(e.message.toString())
                    _isProgramRunning.value = false
                }
            }
            _isProgramRunning.value = true
        }
    }

    fun stopProgram() {
        job?.cancel()
        _isProgramRunning.value = false
    }
}