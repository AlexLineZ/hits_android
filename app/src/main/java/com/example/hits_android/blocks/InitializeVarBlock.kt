package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.*

// Блок создания новой переменной типа Int
class InitializeVarBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "InitVar",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initVarBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var name: String = ""  // Название переменной
    var type: String = ""  // Тип переменной
    var value: String = "" // Значение переменной

    // Создание новой переменной
    override fun runCodeBlock() {
        // Пересоздание переменной
        if (variables[name] != null){
            throw Exception("Чел, ты пересоздаешь переменную");
        }

        // Вычисление значения переменной
        val expression = ParsingFunctions(LexicalComponents(value).getTokensFromCode())
        val newVariable = Variable(name, type, expression.parseExpression()!!.value)

        // Обрезка дробной части у переменной типа Int
        if (type == Type.INT && newVariable.value.toString().contains(".")) {
            newVariable.value = newVariable.value.toString()
                .slice(0.. newVariable.value.toString().indexOf('.') - 1)
        }

        // Обрезание "" у строк
        if (type == Type.STRING) {
            newVariable.value = newVariable.value.toString()
        }

        // Сохранение переменной
        variables[name] = newVariable

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блоков без UI
    fun testBlock(n: String, t: String, v: String){
        name = n
        type = t
        value = v
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}