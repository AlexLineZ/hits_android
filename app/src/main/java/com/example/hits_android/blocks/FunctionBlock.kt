package com.example.hits_android.blocks

import androidx.compose.runtime.Composable
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables

// Блок определения функции
class FunctionBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Function",
    override val isDragOverLocked: Boolean = true
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "functionBlock"
    }

    override val blockName = BLOCK_NAME

    // Область видимости функции
    var functionScope = MutableList<String>(0) { "" }

    // Добавление блока в список блоков
    init {
        // Добавление всех переменных в текущую область видимости
        for (varName in variables.keys) {
            functionScope.add(varName)
        }
        blockList.add(this)
    }

    var parameters = ""                     // Параметры функции
    var funName = ""                        // Название функции
    var parNamesList = MutableList(0) { "" }   // Список параметров

    // Создание функции
    override fun runCodeBlock() {
        val parList = parameters.split(",").toMutableList()

        if (parameters == "") {
            parList.removeAt(0)
        }

        // Инициализация параметров
        for (i in parList.indices) {
            parList[i] = parList[i].replace(" ", "")

            val expression = LexicalComponents(parList[i]).getTokensFromCode()
            val name = expression[0].text
            parNamesList.add(name)
            val type = expression[2].text

            if (variables[name] != null) {
                throw Exception("Параметр назван так же, как и уже существующая переменная.")
            }

            // Добавление параметров в область видимости функции
            variables[name] = Variable(name, type, "null")
            functionScope.add(name)
        }

        // Пропуск тела функции
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

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {

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