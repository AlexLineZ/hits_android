package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.variables
import org.burnoutcrew.reorderable.ReorderableLazyListState

// Блок цикла while
class WhileBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "While",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "WhileBlock"
    }

    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Условие цикла
    var condition: String = ""

    // Выполнение цикла while
    override fun runCodeBlock() {
        // Переход к следующим блокам
        blockIndex++

        // Запоминание индекса блока начала тела цикла
        val whileBeginIndex = blockIndex

        // Проверка условия
        var conditionExpression = ParsingFunctions(LexicalComponents(condition).getTokensFromCode())
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
            conditionExpression = ParsingFunctions(LexicalComponents(condition).getTokensFromCode())
            conditionState = conditionExpression.parseExpression()!!

            // Переход к началу тела цикла
            blockIndex = whileBeginIndex
        }

        // Пропуск тела цикла
        skipBlock()
    }

    // Тестирование блока без UI
    fun testBlock(cond: String) {
        condition = cond
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        item as WhileBlock
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = calculatePadding(codeBlocksList, item.key))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1
                    )
                }
                ItemConditionField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemConditionField(item: WhileBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.condition)) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val themeColors = MaterialTheme.colors

        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color.Black,
                cursorColor = themeColors.primary, // Основной цвет темы
                focusedIndicatorColor = themeColors.primary, // Основной цвет темы
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.condition = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}