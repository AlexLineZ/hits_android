package com.example.hits_android.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow

class FunctionClass(
    var name: String = "myFun",
    val id: Int
) {
    var codeBlocksList by mutableStateOf(
        listOf(
            MainBlock(key = "0", isDragOverLocked = true, title = "Start program"),
            FinishProgramBlock(key = "1", isDragOverLocked = true, title = "End program")
        )
    )

    private val _isOnScreen =  MutableStateFlow(true)
    var isOnScreen: MutableStateFlow<Boolean> = _isOnScreen

    fun setIsOnScreen(newVal: Boolean) {
        _isOnScreen.value = newVal
    }
}