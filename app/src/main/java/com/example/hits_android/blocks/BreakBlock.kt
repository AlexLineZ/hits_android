package com.example.hits_android.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hits_android.expressionParser.scopes
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок выхода из цикла
class BreakBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Break",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object{
        val BLOCK_NAME = "breakBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Выход из цикла
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
                throw Error("Berak не относится к циклу")
            }
        }

        // Выход из цикла
        (blockList[blockIndex] as WhileBlock).testBlock("0;")
        blockList[++blockIndex].runCodeBlock()
        blockIndex--
        skipBlock()
        blockIndex--
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