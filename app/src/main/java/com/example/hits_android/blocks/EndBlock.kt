package com.example.hits_android.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок конца блока кода
class EndBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "End",
    override val isDragOverLocked:Boolean = false
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

    @Composable
    override fun blockComposable(item: Block) {
        Text(
            text = item.title,
            modifier = Modifier.padding(24.dp)
        )
    }
}