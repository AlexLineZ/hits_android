package com.example.hits_android.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables
import com.example.hits_android.model.getStop

class FunctionClass(
    var id: Int,
    mainBlockTitle: String = "Start function",
    finishBlockTitle: String = "End function"
) {
    var codeBlocksList by mutableStateOf(
        listOf(
            MainBlock(key = "0", isDragOverLocked = true, title = mainBlockTitle),
            FinishProgramBlock(key = "1", isDragOverLocked = true, title = finishBlockTitle)
        )
    )

    // Проверка наличия блоков begin end после текущего
    private fun hasBody(): Boolean {
        return blockList[blockIndex].getNameOfBlock() in listOf(
            CallFunctionBlock.BLOCK_NAME,
            ElseBlock.BLOCK_NAME,
            IfBlock.BLOCK_NAME,
            WhileBlock.BLOCK_NAME
        )
    }

    var argsList = mutableListOf<Variable>()

    // Выполнение функции
    fun runFunction(functionList: List<FunctionClass>) {
        blockList = codeBlocksList.toMutableList()
        blockIndex = 0
        variables.clear()

        while (blockIndex < blockList.size && !getStop.value) {
            if (hasBody()) {
                (blockList[blockIndex] as HasBodyBlock).setFunctionList(functionList)
            } else if (blockList[blockIndex].getNameOfBlock() == FunctionsArgumentBlock.BLOCK_NAME) {
                (blockList[blockIndex] as FunctionsArgumentBlock).setArguments(argsList)
            }
            blockList[blockIndex].runCodeBlock()
        }

        argsList.clear()
    }

    // Передача аргументов
    fun setArguments(args: MutableList<Variable>) {
        argsList = args
    }

    fun getName(): String {
        return (codeBlocksList[0] as FunctionNameBlock).getFunName()
    }
}
