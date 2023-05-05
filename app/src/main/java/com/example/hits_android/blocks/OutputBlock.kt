package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions

class OutputBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Print",
    override val isDragOverLocked:Boolean = true
): Block {
    override val blockName = "outputBlock"

    var text: String = ""

    override fun runCodeBlock() {
        when(val result = ParsingFunctions(LexicalComponents(text).getTokensFromCode()).parseExpression()){
            null -> println("ඞ Empty")
            else -> println("ඞ $result")
        }
    }
}