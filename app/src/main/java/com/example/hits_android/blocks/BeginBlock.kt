package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

// Блок начала блока кода
class BeginBlock(
    override var key: String,
    override val title: String = "Begin",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        const val BLOCK_NAME = "beginBlock"
    }

    override val blockName = BLOCK_NAME

    // Вход в новый блок кода
    override fun runCodeBlock() {
        // Создание новой области видимости переменных
        val currentScope = mutableListOf<String>()

        // Добавление всех переменных в текущую область видимости
        for (varName in variables.keys) {
            currentScope.add(varName)
        }

        // Запоминание переменных, входящих в текущую область видимости
        scopes.addScope(currentScope)

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}