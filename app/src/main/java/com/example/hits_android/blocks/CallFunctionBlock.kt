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
                throw Exception("Функция ${functionName} не была найдена при вызове.")
            }
        }

        // Переход к области видимости функции
        scopes.addScope((blockList[blockIndex] as FunctionBlock).getScope())

        // Передача аргументов
        val argList = arguments.split(",").toMutableList()

        if (arguments == "") {
            argList.removeAt(0)
        }

        if (argList.size != (blockList[blockIndex] as FunctionBlock).getParameters().size) {
            throw Exception("Кол-во аргументов при вызове функции ${functionName} не совпадает с кол-вом" +
                    " её параметров.")
        }

        for (i in argList.indices) {
            //argList[i] = argList[i].replace(" ", "")
            argList[i] += ";"

            val expression = ParsingFunctions(LexicalComponents(argList[i]).getTokensFromCode())
            val args = expression.parseExpression()!!

            if (variables[(blockList[blockIndex] as FunctionBlock).getParameters()[i]]?.type != args.type) {
                throw Exception("При вызове функции ${functionName} переданы аргументы неподходящих типов.")
            }

            variables[(blockList[blockIndex] as FunctionBlock).getParameters()[i]]?.value = args.value
        }

        // Выполнение тела функции
        blockIndex += 1

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