package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import com.example.hits_android.TestProgram
import com.example.hits_android.blocks.blockIndex
import com.example.hits_android.blocks.blockList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

var _output = MutableStateFlow("")

class FlowViewModel : ViewModel() {
    val output: StateFlow<String> = _output.asStateFlow()

    fun setCurrentValue(newValue: String) {
        _output.value += newValue
        println(_output.value)
    }
    private val _isProgramRunning = MutableStateFlow(false)
    val isProgramRunning: StateFlow<Boolean> = _isProgramRunning.asStateFlow()

    private var job: Job? = null

    fun toggleProgramRunning() {
        _isProgramRunning.value = !_isProgramRunning.value
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startProgram() {
        if (!isProgramRunning.value) {
            job = GlobalScope.launch {
                TestProgram()
                while (blockIndex < blockList.size) {
                    blockList[blockIndex].runCodeBlock()
                }
                _isProgramRunning.value = false
            }
            _isProgramRunning.value = true
        }
    }

    fun stopProgram() {
        job?.cancel()
        _isProgramRunning.value = false
    }
}