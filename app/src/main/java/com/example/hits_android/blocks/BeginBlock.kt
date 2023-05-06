package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

class BeginBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Assign",
    override val isDragOverLocked:Boolean = true
): Block {
    override val blockName = "beginBlock"

    override fun runCodeBlock() {
        var currentScope = mutableListOf<String>()

        for (varName in variables.keys) {
            currentScope.add(varName)
        }

        scopes.addScope(currentScope)
    }
}