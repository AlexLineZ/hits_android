package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables

class InitializeBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Init",
    override val isDragOverLocked:Boolean = true
): Block {

    override val blockName = "initBlock"

    var name: String = ""
    var type: String = ""
    var value: String = ""

    // Тестирование блоков без UI
    fun textBlock(n: String, t: String, v: String){
        name = n
        type = t
        value = v
    }

    override fun runCodeBlock() {
        if (variables[name] != null){
            throw Exception("Чел, ты пересоздаешь переменную на блоке ${(nextID - 1)}");
        }
        else {
            var sss = ParsingFunctions(LexicalComponents(value).getTokensFromCode())
            var idk = sss.parseExpression()
            variables[name] = idk!!
            //variables[name] = value
        }
    }
}