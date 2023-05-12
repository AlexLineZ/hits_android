package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

// Блок цикла while
class WhileBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "While",
    override val isDragOverLocked:Boolean = true
):Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "WhileBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Условие цикла
    var condition: String = ""

    // Выполнение цикла while
    override fun runCodeBlock() {
        // Переход к следующим блокам
        blockIndex++

        // Запоминание индекса блока начала тела цикла
        val whileBeginIndex = blockIndex

        // Проверка условия
        var conditionExpression = ParsingFunctions(LexicalComponents(condition).getTokensFromCode())
        var conditionState = conditionExpression.parseExpression()!!

        // Пока условие верно
        while (conditionState.value == "1") {
            // Выполнение тела while
            while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
                blockList[blockIndex].runCodeBlock()
            }

            // Удаление переменных, которые были созданы внутри тела while
            blockList[blockIndex].runCodeBlock()

            // Проверка условия
            conditionExpression = ParsingFunctions(LexicalComponents(condition).getTokensFromCode())
            conditionState = conditionExpression.parseExpression()!!

            // Переход к началу тела цикла
            blockIndex = whileBeginIndex
        }

        // Пропуск тела цикла
        skipBlock()
    }

    // Тестирование блока без UI
    fun testBlock(cond: String) {
        condition = cond
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}