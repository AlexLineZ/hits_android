package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.scopes

// Блок перехода к следующей итерации цикла
class ContinueBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Continue",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object{
        val BLOCK_NAME = "continueBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Переход к следующей итерации цикла
    override fun runCodeBlock() {
        // Поиск начала тела цикла
        while (blockList[blockIndex].getNameOfBlock() != WhileBlock.BLOCK_NAME) {
            try {
                blockIndex--

                if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
                    scopes.destoryScope()
                }
            }
            catch (e: ArrayIndexOutOfBoundsException) {
                throw Exception("Continue не относится к циклу")
            }
        }

        // Пропуск тела цикла
        blockList[++blockIndex].runCodeBlock()
        blockIndex--
        skipBlock()
        blockIndex--
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}