package com.example.hits_android.blocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableLazyListState

interface Block {
    val blockName: String
    var previousID: Int
    var nextID: Int
    val key: String
    val title: String
    val isDragOverLocked: Boolean
    fun runCodeBlock()
    fun getNameOfBlock(): String

    @Composable
    fun BlockComposable(item: Block, codeBlocksList: List<Block>)
}

class MainBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Main",
    override val isDragOverLocked: Boolean = false
) : Block {
    override val blockName = "mainBlock"

    override fun runCodeBlock() {

    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Text(
            text = item.title,
            modifier = Modifier.padding(24.dp)
        )
    }
}

class FinishProgramBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Main",
    override val isDragOverLocked: Boolean = false
) : Block {
    override val blockName = "finishProgram"

    override fun runCodeBlock() {

    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Text(
            text = item.title,
            modifier = Modifier.padding(24.dp)
        )
    }
}

fun calculatePadding(codeBlocksList: List<Block>, key: String): Dp {
    var paddingValue: Int = 0
    var flag: Boolean = false
    for (block in codeBlocksList) {
        when (block.key) {
            key -> {
                flag = true
                when (block.blockName) {
                    "beginBlock" -> paddingValue += 25
                    "endBlock" -> {}
                    else -> {}
                }
            }

            else -> {
                when (block.blockName) {
                    "beginBlock" -> paddingValue += 25
                    "endBlock" -> paddingValue -= 25
                    else -> {}
                }
            }
        }
        if (flag) {
            break
        }
        paddingValue = (if (paddingValue >= 0) (paddingValue) else (0))
    }
    return paddingValue.dp
}