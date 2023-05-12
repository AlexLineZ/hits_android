package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables

// Блок создания новой переменной типа Int
class InitializeIntBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "InitInt",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initIntBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var name: String = ""  // Название переменной
    var value: String = "" // Значение переменной

    // Создание новой переменной
    override fun runCodeBlock() {
        // Пересоздание переменной
        if (variables[name] != null){
            throw Exception("Чел, ты пересоздаешь переменную");
        }

        // Создание переменной типа Int
        val expression = ParsingFunctions(LexicalComponents(value).getTokensFromCode())
        variables[name] = Variable(name, "Int", expression.parseExpression()!!.value)

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блоков без UI
    fun testBlock(n: String, v: String){
        name = n
        value = v
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return value
    }
}