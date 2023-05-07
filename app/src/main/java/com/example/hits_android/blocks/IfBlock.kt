package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

// Блок условия
class IfBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "If",
    override val isDragOverLocked:Boolean = true
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
        if (conditionState == 1) {
            // Выполнение тела if
            while (blockList[blockIndex - 1].getNameOfBlock() != EndBlock.BLOCK_NAME) {
                blockList[blockIndex].runCodeBlock()
            }

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
}