package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.*

// Блок вызова функции
class CallFunctionBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "CallFunction",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "callFunctionBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var functionName = "" // Название вызываемой функции
    var arguments = ""    // Передаваемые аргументы

    // Вызов функции
    override fun runCodeBlock() {
        println(arguments)

        // Запоминание места вызова функции
        val callingIndex = blockIndex

        // Поиск определения функции
        while (blockList[blockIndex].getNameOfBlock() != FunctionBlock.BLOCK_NAME ||
            (blockList[blockIndex].getNameOfBlock() == FunctionBlock.BLOCK_NAME &&
                    (blockList[blockIndex] as FunctionBlock).getFunctionName() != functionName)) {
            try {
                blockIndex--

                if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
                    scopes.destoryScope()
                }
                else if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
                    scopes.addScope(scopes.getScope())
                }
            }
            catch (e: ArrayIndexOutOfBoundsException) {
                throw Exception("Вызываемая функция не была найдена.")
            }
        }

        // Переход к области видимости функции
        scopes.addScope((blockList[blockIndex] as FunctionBlock).getScope())

        // Передача аргументов
        val argList = arguments.split(",").toMutableList()

        for (i in argList.indices) {
            argList[i] = argList[i].replace(" ", "")
            argList[i] += ";"

            val expression = ParsingFunctions(LexicalComponents(argList[i]).getTokensFromCode())
            variables[(blockList[blockIndex] as FunctionBlock).getParameters()[i]]?.value = expression.parseExpression()!!.value
        }

        // Выполнение тела функции
        blockIndex += 2

        while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
            blockList[blockIndex].runCodeBlock()
        }

        // Удаление переменных, которые были созданы внутри тела if
        blockList[blockIndex].runCodeBlock()

        // Возврат на место вызова функции
        blockIndex = callingIndex + 1
    }

    // Тестирование без UI
    fun testBlock(funName: String, args: String) {
        functionName = funName
        arguments = args
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}