package com.example.hits_android.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FunctionClass(
    var functionName: String = "myFun",
    var isOnScreen: Boolean = true,
    val id: Int
) {
    var codeBlocksList by mutableStateOf(
        listOf(
            MainBlock(key = "0", isDragOverLocked = true, title = "Start program"),
            FinishProgramBlock(key = "1", isDragOverLocked = true, title = "End program")
        )
    )
}