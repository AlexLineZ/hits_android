package com.example.hits_android.blocks

import androidx.compose.runtime.Composable
import com.google.gson.InstanceCreator
import java.lang.reflect.Type


class BlockImpl : Block {
    override fun runCodeBlock() {
        // Реализация метода runCodeBlock()
    }

    override fun getNameOfBlock(): String {
        // Реализация метода getNameOfBlock()
        return ""
    }

    @Composable
    override fun BlockComposable(item: Block) {
        // Реализация метода BlockComposable()
    }

    override var blockName: String = ""
    override var key: String = ""
    override var title: String = ""
    override var isDragOverLocked: Boolean = false

    var condition: String = ""
    var conditionText: String = ""
    var beginKey: String = ""
    var endKey: String = ""

    var expression: String = ""

    var name: String = ""
    var type: String = ""
    var value: String = ""

    var arrayName: String = ""
    var arrayType: String = ""
    var arraySize: String = ""

    var parameters: String = ""

    var functionName: String = ""
    var arguments: String = ""

    var variableName: String = ""
    var newValue: String = ""
}

class BlockInstanceCreator : InstanceCreator<Block> {
    override fun createInstance(type: Type): Block {
        return BlockImpl()
    }
}
