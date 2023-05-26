package com.example.hits_android.blocks

var blockList = mutableListOf<Block>() // Список блоков
var blockIndex = 0                     // Индекс текущего выполняемого блока

// Проверка последовательности блоков Begin и End
fun checkBeginEnd(functionList:  List<FunctionClass>) {
    var funcIndex = 0
    var balance = 0

    while (funcIndex < functionList.size) {
        blockList = functionList[funcIndex].codeBlocksList.toMutableList()
        var blockInd = 0

        // Проверка наличия тела у блоков If, Else, While
        while (blockInd < blockList.size) {
            if ((blockList[blockInd].getNameOfBlock() == IfBlock.BLOCK_NAME ||
                        blockList[blockInd].getNameOfBlock() == ElseBlock.BLOCK_NAME ||
                        blockList[blockInd].getNameOfBlock() == WhileBlock.BLOCK_NAME) &&
                (blockInd == blockList.size ||
                        blockList[blockInd + 1].getNameOfBlock() != BeginBlock.BLOCK_NAME)){
                throw Exception("У блока ${blockList[blockInd].getNameOfBlock()} нет тела")
            }

            // Проверка соответствия блоков Begin и End
            if (blockList[blockInd].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
                balance++
            }
            else if (blockList[blockInd].getNameOfBlock() == EndBlock.BLOCK_NAME) {
                balance--
            }

            if (balance < 0) {
                throw Exception("У блока End нет соответствующего блока Begin")
            }

            blockInd++
        }

        funcIndex++
    }
}

// Пропуск блока кода
fun skipBlock() {
    var currentBalance = 0

    do {
        if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
            currentBalance++
        } else if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
            currentBalance--
        }

        blockIndex++
    } while (currentBalance != 0)
}
