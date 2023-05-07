package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

// Блок создания новой переменной
class InitializeBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Init",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initBlock"
    }
    override val blockName = BLOCK_NAME

    var name: String = ""  // Название переменной
    var type: String = ""  // Тип переменной
    var value: String = "" // Значение переменной

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Тестирование блоков без UI
    fun testBlock(n: String, t: String, v: String){
        name = n
        type = t
        value = v
    }

    // Создание новой переменной
    override fun runCodeBlock() {
        // Пересоздание переменной
        if (variables[name] != null){
            throw Exception("Чел, ты пересоздаешь переменную");
        }
        // Создание переменной типа Int
        else if (type == "Int") {
            val expression = ParsingFunctions(LexicalComponents(value).getTokensFromCode())
            variables[name] = expression.parseExpression()!!
        }
        // Создание переменной типа Array
        else if (type == "Array") {
            val expression = ParsingFunctions(LexicalComponents(value).getTokensFromCode())
            variables[name] = Array(expression.parseExpression() as Int) {0}
        }
        // Использование иного типа данных
        else {
            throw Error("Неизвестный тип")
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return value
    }
}