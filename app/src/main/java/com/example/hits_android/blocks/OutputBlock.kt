package com.example.hits_android.blocks

import androidx.lifecycle.ViewModel
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables
import com.example.hits_android.model.FlowViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

// Блок вывода
class OutputBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Print",
    override val isDragOverLocked:Boolean = false
): Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "outputBlock"
    }
    override val blockName = BLOCK_NAME

    // Выражение, переданное блоку в качестве параметра
    var expression: String = ""

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Вывод в консоль
    override fun runCodeBlock() {
        val exp = ParsingFunctions(LexicalComponents(expression).getTokensFromCode())
        val result = exp.parseExpression()

        when(result){
            null -> println("ඞ Empty")
            else -> {
                //println("ඞ ${result.value}")
                FlowViewModel().setCurrentValue("ඞ ${result.value}\n")
            }
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блока без UI
    fun testBlock(exp: String) {
        expression = exp
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }
}
