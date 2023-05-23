package com.example.hits_android.blocks

import androidx.compose.runtime.Composable
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
    fun BlockComposable(item: Block)
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