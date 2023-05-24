package com.example.hits_android.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables
import com.example.hits_android.model.getStop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FunctionClass(
    name: String = "MyFun",
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
    var varList = mutableListOf<Variable>()

    // Выполнение функции
    fun runFunction(functionList:  List<FunctionClass>) {
        blockList = codeBlocksList.toMutableList()
        blockIndex = 0
        variables.clear()

        while (blockIndex < blockList.size && !getStop.value) {
            if (blockList[blockIndex].getNameOfBlock() == CallFunctionBlock.BLOCK_NAME) {
                (blockList[blockIndex] as CallFunctionBlock).setFunctionList(functionList)
            }
            else if (blockList[blockIndex].getNameOfBlock() == FunctionsArgumentBlock.BLOCK_NAME) {
                (blockList[blockIndex] as FunctionsArgumentBlock).setArguments(varList)
            }
            blockList[blockIndex].runCodeBlock()
        }

        varList.clear()
    }

    // Передача аргументов
    fun setArguments(args: MutableList<Variable>) {
        varList = args
    }

    fun getName(): String {
        return (codeBlocksList[0] as FunctionNameBlock).getFunName()
    }

    fun setCurrentScreenId(newName: String) {
        _functionName.value = newName
    }
}
