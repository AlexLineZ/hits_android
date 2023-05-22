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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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