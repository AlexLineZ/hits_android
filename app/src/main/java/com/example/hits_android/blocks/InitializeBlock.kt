package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

class InitializeBlock: Block {
    override val blockName = "initBlock"
    override var previousID = -1
    override var nextID = -1

    var name: String = ""
    var type: String = ""
    var value: String = ""

    override fun runCodeBlock() {
        if (variables[name] != null){
            throw Error("Чел, ты пересоздаешь переменную на блоке ${(nextID - 1)}");
        }
        else {
            variables[name] = ParsingFunctions(LexicalComponents(value).getTokensFromCode()).parseExpression()!!
        }
    }
}