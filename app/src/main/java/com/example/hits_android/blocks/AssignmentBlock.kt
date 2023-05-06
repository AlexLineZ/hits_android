package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

// Блок присвоения переменной нового значения
class AssignmentBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Assign",
    override val isDragOverLocked:Boolean = true
): Block {

    // Название блока
    companion object {
        val BLOCK_NAME = "assignmentBlock"
    }
    override val blockName = BLOCK_NAME

    var variableName: String = "" // Название изменяемой переменной
    var newValue: String = ""     // Новое значение изменяемой переменной

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Присваивание переменной нового значения
    override fun runCodeBlock() {
        if (variables[variableName] != null){
            var expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            variables[variableName] = expression.parseExpression()!!
        }
        else {
            throw Exception("Чел, ты хочешь присвоить той переменной, которой нет...")
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блоков без UI
    fun testBlock(oldVal: String, newVal: String) {
        variableName = oldVal
        newValue = newVal
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}