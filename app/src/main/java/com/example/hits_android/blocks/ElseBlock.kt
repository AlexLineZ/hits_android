package com.example.hits_android.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок else
class ElseBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Else",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "ElseBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Выполнение блока else
    override fun runCodeBlock() {
        // Переход к следующим блокам
        blockIndex++

        // Выполнение тела блока else
        while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
            blockList[blockIndex].runCodeBlock()
        }

        // Удаление переменных, которые были созданы внутри тела else
        blockList[blockIndex].runCodeBlock()
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun blockComposable(item: Block) {
        Text(
            text = item.title,
            modifier = Modifier.padding(24.dp)
        )
    }
}