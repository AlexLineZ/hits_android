package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.scopes

// Блок выхода из цикла
class BreakBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Break",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
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
                    scopes.destroyScope()
                } else if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
                    scopes.addScope(scopes.getScope())
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw Exception("Break не относится к циклу")
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
    override fun BlockComposable(item: Block) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(50.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}