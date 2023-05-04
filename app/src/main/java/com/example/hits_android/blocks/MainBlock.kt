package com.example.hits_android.blocks

import androidx.compose.runtime.Composable


interface Block {
    val blockName: String
    var previousID: Int
    var nextID: Int
    fun runCodeBlock()
}

class MainBlock: Block {
    override val blockName = "mainBlock"
    override var previousID = -1
    override var nextID = -1

    override fun runCodeBlock() {

    }
}

@Composable
fun MainBlockComposable() {

}

