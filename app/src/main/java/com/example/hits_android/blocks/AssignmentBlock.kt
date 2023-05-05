package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

class AssignmentBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Assign",
    override val isDragOverLocked:Boolean = true
): Block {

    override val blockName = "mainBlock"

    var oldValue: String = ""
    var newValue: String = ""

    override fun runCodeBlock() {
        val newVarValue = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode()).parseExpression()!!
        if (variables[oldValue] != null){
            variables[oldValue] = newVarValue
        }
        else {
            throw Exception("Чел, ты хочешь присвоить той переменной, которой нет...")
        }
    }
}