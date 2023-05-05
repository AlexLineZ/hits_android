package com.example.hits_android.blocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



interface Block {
    val blockName: String
    var previousID: Int
    var nextID: Int
    val key: String
    val title: String
    val isDragOverLocked:Boolean
    fun runCodeBlock()
}

class MainBlock(
    override var previousID: Int = -1,
    override var nextID:Int = -1,
    override val key: String,
    override val title:String = "Main",
    override val isDragOverLocked:Boolean = true
): Block {
    override val blockName = "mainBlock"

    override fun runCodeBlock() {

    }
}