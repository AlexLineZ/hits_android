package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

// Блок конца блока кода
class EndBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Assign",
    override val isDragOverLocked:Boolean = true
): Block {
    // Название блока
    companion object{
        val BLOCK_NAME = "endBlock"
    }
    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init{
        blockList.add(this)
    }

    // Выход из блока кода
    override fun runCodeBlock() {

        // Уничтожение переменных, объявленных в текущем блоке кода
        for (name in variables.keys) {
            if (name !in scopes.getScope()) {
                variables.remove(name)
            }
        }

        // Возврат к предыдущей области видимости переменных
        scopes.destoryScope()

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}