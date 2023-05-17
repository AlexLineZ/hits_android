package com.example.hits_android.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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