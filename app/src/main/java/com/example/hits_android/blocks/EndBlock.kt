package com.example.hits_android.blocks

import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

class EndBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title:String = "Assign",
    override val isDragOverLocked:Boolean = true
): Block {
    override val blockName = "endBlock"

    override fun runCodeBlock() {

        for (name in variables.keys) {
            if (name !in scopes.getScope()) {
                variables.remove(name)
            }
        }

        scopes.destoryScope()
//        variables.forEach { t, u ->
//            if (t !in scopes.getScope()) {
//                variables.remove(t)
//            }
//        }
    }
}