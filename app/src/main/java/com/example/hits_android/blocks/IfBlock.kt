package com.example.hits_android.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок условия
class IfBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "If",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "IfBlock"
    }
    override val blockName = BLOCK_NAME

    // Условие
    var condition: String = ""

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Выполнение блока If
    override fun runCodeBlock() {
        // Выполнение следующих блоков
        blockIndex++

        // Проверка условия
        val conditionExpression = ParsingFunctions(LexicalComponents(condition).getTokensFromCode())
        val conditionState = conditionExpression.parseExpression()!!

        // Если условие верно
        if (conditionState.value == "1") {
            // Выполнение тела if
            while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
                blockList[blockIndex].runCodeBlock()
            }

            // Удаление переменных, которые были созданы внутри тела if
            blockList[blockIndex].runCodeBlock()

            // Пропуск else
            if (blockList[blockIndex].getNameOfBlock() == ElseBlock.BLOCK_NAME) {
                blockIndex++
                skipBlock()
            }
        }
        // Если условие неверно
        else {
            // Пропуск тела if
            skipBlock()
        }
    }

    // Тестирование блока без UI
    fun testBlock(cond: String) {
        condition = cond
    }

    // Возвращение названия блока
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