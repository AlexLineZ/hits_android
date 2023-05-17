package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hits_android.expressionParser.scopes
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок перехода к следующей итерации цикла
class ContinueBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Continue",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
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
            } catch (e: ArrayIndexOutOfBoundsException) {
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

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = calculatePadding(codeBlocksList, item.key))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Gray)
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}