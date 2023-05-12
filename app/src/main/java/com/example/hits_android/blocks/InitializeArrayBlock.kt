package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

// Блок создания массива
class InitializeArrayBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "InitArray",
    override val isDragOverLocked:Boolean = false
):Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initArrayBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var arrayName = ""   // Название массива
    var arraySize = ""   // Размер массива

    // Создание массива
    override fun runCodeBlock() {
        // Пересоздание массива
        if (variables[arrayName] != null) {
            throw Error("Чел, ты пересоздаешь переменную")
        }

        // Создание массива
//        val expression = ParsingFunctions(LexicalComponents(arraySize).getTokensFromCode())
//        variables[arrayName] = Array(expression.parseExpression()!!){0}

        // Выполнение следующих блоков
        blockIndex++
    }

    // Тестирование без UI
    fun testBlock(name: String, size: String) {
        arrayName = name
        arraySize = size
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}