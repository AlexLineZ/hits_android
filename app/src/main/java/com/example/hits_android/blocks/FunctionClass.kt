package com.example.hits_android.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FunctionClass(
    name: String = "myFun",
    val id: Int,
    mainBlockTitle: String = "Start function",
    finishBlockTitle: String = "End function"
) {
    var codeBlocksList by mutableStateOf(
        listOf(
            MainBlock(key = "0", isDragOverLocked = true, title = mainBlockTitle),
            FinishProgramBlock(key = "1", isDragOverLocked = true, title = finishBlockTitle)
        )
    )

    private var _functionName = MutableStateFlow(name)
    val functionName: StateFlow<String> = _functionName.asStateFlow()

    val idk = 0

    fun setCurrentScreenId(newName: String) {
        _functionName.value = newName
        val idkkk = idk
    }
}
