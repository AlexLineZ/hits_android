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
        // Индекс и название массива
        var arrayIndex = -1
        var arrName = ""

        // Нахождение индекса массива
        if ('[' in variableName) {
            val index = variableName.slice((variableName.indexOf('[') + 1)..(variableName.indexOf(']') - 1))
            val indexExpression = ParsingFunctions(LexicalComponents(index + ";").getTokensFromCode())

            arrayIndex = (indexExpression).parseExpression()!!.value.toString().toInt()
            arrName = variableName.slice(0..variableName.indexOf('[') - 1)
        }

        // Проверка существования переменной
        if (variables[variableName] == null && variables[arrName] == null) {
            throw Exception("Чел, ты хочешь присвоить той переменной, которой нет...")
        }

        // Присвоение значения переменной типа Int
        if (arrayIndex == -1) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            variables[variableName] = expression.parseExpression()!!
        }

        // Присвоение значения элементу массива
        else if ((variables[arrName]?.value as Array<*>)[0] is Int) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<Int>)[arrayIndex] = expression.parseExpression()!!.value.toString().toInt()
        }
        else if ((variables[arrName]?.value as Array<*>)[0] is Double) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<Double>)[arrayIndex] = expression.parseExpression()!!.value.toString().toDouble()
        }
        else {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<String>)[arrayIndex] = expression.parseExpression()!!.value.toString()
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