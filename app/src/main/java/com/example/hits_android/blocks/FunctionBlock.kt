package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.*
import kotlin.math.exp

// Блок определения функции
class FunctionBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Function",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "functionBlock"
    }
    override val blockName = BLOCK_NAME

    // Область видимости функции
    var functionScope = MutableList<String>(0){""}

    // Добавление блока в список блоков
    init {
        // Добавление всех переменных в текущую область видимости
        for (varName in variables.keys) {
            functionScope.add(varName)
        }
        blockList.add(this)
    }

    var parameters = ""
    var funName = ""
    var parNamesList = MutableList(0){""}

    // Создание функции
    override fun runCodeBlock() {
        val parList = parameters.split(",").toMutableList()

        for (i in parList.indices) {
            parList[i] = parList[i].replace(" ", "")

            // Вычисление значения переменной
            val expression = LexicalComponents(parList[i]).getTokensFromCode()
            val name = expression[0].text
            parNamesList.add(name)
            val type = expression[2].text

            if (variables[name] != null) {
                throw Exception("Параметр назван так же, как и уже существующая переменная.")
            }

            variables[name] = Variable(name, type, "null")
            functionScope.add(name)
        }

        println(functionScope)

        blockIndex++
        skipBlock()
    }

    // Тестирование без UI
    fun testBlock(name: String, p: String) {
        funName = name
        parameters = p
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    // Возврат названия функции
    fun getFunctionName(): String {
        return funName
    }

    // Возврат области видимости функции
    fun getScope(): MutableList<String> {
        return functionScope
    }

    // Возврат списка параметров
    fun getParameters(): MutableList<String> {
        return parNamesList
    }
}