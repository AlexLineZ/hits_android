package com.example.hits_android.blocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
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
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                modifier = Modifier
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )
        }
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