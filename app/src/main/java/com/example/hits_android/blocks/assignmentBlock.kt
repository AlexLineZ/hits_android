package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

//class assignmentBlock: Block {
//    override val blockName = "assBlock"
//    override var previousID = -1
//    override var nextID = -1
//
//    var oldValue: String = ""
//    var newValue: String = ""
//
//    override fun runCodeBlock() {
//        val newVarValue = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode()).parseExpression()!!
//        if (variables[oldValue] != null){
//            variables[oldValue] = newVarValue
//        }
//        else {
//            throw Error("Чел, ты хочешь присвоить той переменной, которой нет...")
//        }
//    }
//}