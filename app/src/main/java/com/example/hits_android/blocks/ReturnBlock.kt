package com.example.hits_android.blocks

import androidx.compose.runtime.Composable

// Блок выхода из функции
class ReturnBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override var key: String,
    override val title: String = "Return",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "returnBlock"
    }
    override val blockName = BLOCK_NAME

    // Пропуск выполнения функции
    override fun runCodeBlock() {
        blockIndex = blockList.size - 1
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }


    @Composable
    override fun BlockComposable(item: Block) {

    }
}