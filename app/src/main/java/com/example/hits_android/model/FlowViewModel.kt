package com.example.hits_android.model

import android.os.Looper
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.blockIndex
import com.example.hits_android.blocks.blockList
import com.example.hits_android.expressionParser.variables
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

var _output = MutableStateFlow("")
var isOutputRunning = MutableStateFlow(false)
var getStop = MutableStateFlow(false)

class FlowViewModel : ViewModel() {
    val output: StateFlow<String> = _output.asStateFlow()

    fun setCurrentValue(newValue: String) {
        _output.value += newValue
    }

    fun setError(error: String) {
        _output.value = error
    }

    private val _isProgramRunning = MutableStateFlow(false)
    val isProgramRunning: StateFlow<Boolean> = _isProgramRunning.asStateFlow()

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun startProgram() {
        if (!isProgramRunning.value) {
            job = GlobalScope.launch {
                try {
                    variables.clear()
                    blockIndex = 0
                    _output.value = ""
                    getStop.value = false

                    while (blockIndex < blockList.size && !getStop.value) {
                        blockList[blockIndex].runCodeBlock()
                    }

                    if (!isOutputRunning.value && !getStop.value){
                        isOutputRunning.value = true
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            setCurrentValue("\nПроцесс (${(Math.random() * 10000).toInt()}) завершил работу с кодом 0.")
                            isOutputRunning.value = false
                        }, 1000)
                    }

                    _isProgramRunning.value = false
                } catch (e: java.lang.Exception) {
                    setError("Error: " + e.message.toString())
                    _isProgramRunning.value = false
                }
            }
            _isProgramRunning.value = true
        }
    }

    fun stopProgram() {
        getStop.value = true
        job?.cancel()
        _isProgramRunning.value = false
    }
}