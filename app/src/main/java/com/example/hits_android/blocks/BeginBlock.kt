package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

// Блок начала блока кода
class BeginBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Begin",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object{
        val BLOCK_NAME = "beginBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Вход в новый блок кода
    override fun runCodeBlock() {
        // Создание новой области видимости переменных
        var currentScope = mutableListOf<String>()

        // Добавление всех переменных в текущую область видимости
        for (varName in variables.keys) {
            currentScope.add(varName)
        }

        // Запоминание переменных, входящих в текущую область видимости
        scopes.addScope(currentScope)

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}