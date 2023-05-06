package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions

// Блок вывода
class OutputBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Print",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "outputBlock"
    }
    override val blockName = BLOCK_NAME

    // Выражение, переданное блоку в качестве параметра
    var expression: String = ""

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Вывод в консоль
    override fun runCodeBlock() {
        when(val result = ParsingFunctions(LexicalComponents(expression).getTokensFromCode()).parseExpression()){
            null -> println("ඞ Empty")
            else -> println("ඞ $result")
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}