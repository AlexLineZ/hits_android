package com.example.hits_android.blocks

var blockList = mutableListOf<Block>() // Список блоков
var blockIndex = 0                     // Индекс текущего выполняемого блока

// Пропуск блока кода
fun skipBlock() {
    var currentBalance = 0

    do {
        if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
            currentBalance++
        }
        else if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
            currentBalance--
        }

        blockIndex++
    } while (currentBalance != 0)
}
