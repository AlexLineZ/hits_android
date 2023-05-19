package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.scopes

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
                } else if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
                    scopes.addScope(scopes.getScope())
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
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}