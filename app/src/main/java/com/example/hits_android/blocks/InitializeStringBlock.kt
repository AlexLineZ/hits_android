package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.variables

// Создание строки
class InitializeStringBlock (
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "InitStr",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initStrBlock"
    }
    override val blockName = BLOCK_NAME

    init {
        blockList.add(this)
    }

    var name = ""
    var str = ""

    // Создание строки
    override fun runCodeBlock() {
        if (variables[name] != null) {
            throw Error("Чел, ты пересоздаешь переменную")
        }

        //variables[name] = str

        blockIndex++
    }

    // Тестирование без UI
    fun testBlock(n: String, s: String){
        name = n
        str = s
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}