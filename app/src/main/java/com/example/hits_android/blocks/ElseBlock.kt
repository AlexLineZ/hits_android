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

// Блок else
class ElseBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Else",
    override val isDragOverLocked: Boolean = false
) : Block {
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
        // Запоминание позиции Else блока
        val savedIndex = blockIndex

        // Проверка наличия End блока перед Else
        blockIndex--

        if (blockIndex == -1 || blockList[blockIndex].getNameOfBlock() != BLOCK_NAME) {
            throw Exception("Else не относится к условию.")
        }

        // Проверка того, что Else идёт после If
        var balance = 0

        do {
            if (blockList[blockIndex].getNameOfBlock() == BLOCK_NAME) {
                balance++
            } else if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
                balance--
            }

            blockIndex--
        } while (balance != 0 && blockIndex < 0)

        if (blockIndex == -1 || blockList[blockIndex].getNameOfBlock() != IfBlock.BLOCK_NAME) {
            throw Exception("Else не относится к условию.")
        }

        // Возвращение к телу блока Else
        blockIndex = savedIndex + 1

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