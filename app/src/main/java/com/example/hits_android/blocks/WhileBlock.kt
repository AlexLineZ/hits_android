package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions

// Блок цикла while
class WhileBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "While",
    override val isDragOverLocked: Boolean = false,
    val beginKey: String = "",
    val endKey: String = ""
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "WhileBlock"
    }

    override val blockName = BLOCK_NAME

    // Условие цикла
    var condition: String = ""
    var conditionText: String = ""

    // Выполнение цикла while
    override fun runCodeBlock() {
        // Переход к следующим блокам
        blockIndex++

        // Запоминание индекса блока начала тела цикла
        val whileBeginIndex = blockIndex

        // Проверка условия
        var conditionExpression =
            ParsingFunctions(LexicalComponents(condition + ";").getTokensFromCode())
        var conditionState = conditionExpression.parseExpression()!!

        // Пока условие верно
        while (conditionState.value == "1") {
            // Выполнение тела while
            while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
                blockList[blockIndex].runCodeBlock()
            }

            // Удаление переменных, которые были созданы внутри тела while
            blockList[blockIndex].runCodeBlock()

            // Проверка условия
            conditionExpression =
                ParsingFunctions(LexicalComponents(condition + ";").getTokensFromCode())
            conditionState = conditionExpression.parseExpression()!!

            // Переход к началу тела цикла
            blockIndex = whileBeginIndex
        }

        // Пропуск тела цикла
        skipBlock()
        condition = conditionText
    }

    // Изменение условия
    fun changeCondition(cond: String) {
        condition = cond
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as WhileBlock
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(70.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = item.title,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                ItemConditionField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemConditionField(item: WhileBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.conditionText)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.conditionText = textState.value.text
                item.condition = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "condition", maxLines = 1) },
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.conditionText = textState.value.text
                    item.condition = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}