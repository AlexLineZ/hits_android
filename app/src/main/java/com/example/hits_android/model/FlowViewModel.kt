package com.example.hits_android.model

import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.*
import com.example.hits_android.expressionParser.variables
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

var _output = MutableStateFlow("")
var isOutputRunning = MutableStateFlow(false)
var getStop = MutableStateFlow(false)
var newInput = MutableStateFlow("")
var isTextFieldVisible = MutableStateFlow(false)

class FlowViewModel : ViewModel() {
    val output: StateFlow<String> = _output.asStateFlow()

    fun setCurrentValue(newValue: String) {
        _output.value += newValue
    }

    fun setInput(newValue: String){
        newInput.value = newValue
    }

    fun clearInput(){
        newInput.value = ""
    }

    private fun setError(error: String) {
        _output.value = error
    }

    fun changeVisibilityTextField(){
        isTextFieldVisible.value = !isTextFieldVisible.value
    }

    // Проверка наличия блоков begin end после текущего
    private fun hasBody(): Boolean {
        return blockList[blockIndex].getNameOfBlock() in listOf<String>(
            CallFunctionBlock.BLOCK_NAME,
            ElseBlock.BLOCK_NAME,
            IfBlock.BLOCK_NAME,
            WhileBlock.BLOCK_NAME
        )
    }

    private val _isProgramRunning = MutableStateFlow(false)
    val isProgramRunning: StateFlow<Boolean> = _isProgramRunning.asStateFlow()

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun startProgram(functionList: List<FunctionClass>) {

        if (!isProgramRunning.value) {
            job = GlobalScope.launch {
                try {
                    variables.clear()
                    blockIndex = 0
                    _output.value = ""
                    getStop.value = false
                    newInput.value = ""
                    isTextFieldVisible.value = false

                    // Проверка последовательности блоков Begin и End
                    checkBeginEnd(functionList)

                    blockList = functionList[0].codeBlocksList.toMutableList()
                    while (blockIndex < blockList.size && !getStop.value) {
                        if (hasBody()) {
                            (blockList[blockIndex] as HasBodyBlock).setFunctionList(functionList)
                        }

                        blockList[blockIndex].runCodeBlock()
                    }

                    if (!isOutputRunning.value && !getStop.value) {
                        isOutputRunning.value = true
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            setCurrentValue("\nProcess (${(Math.random() * 10000).toInt()}) exited with code 0.")
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
        isTextFieldVisible.value = false
        getStop.value = true
        job?.cancel()
        _isProgramRunning.value = false
    }
}