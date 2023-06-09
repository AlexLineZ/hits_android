package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    override var key: String,
    override val title: String = "While",
    override val isDragOverLocked: Boolean = false,
    val beginKey: String = "",
    val endKey: String = ""
) : Block, HasBodyBlock {
    // Название блока
    companion object {
        const val BLOCK_NAME = "whileBlock"
    }

    // Проверка наличия блоков begin end после текущего
    private fun hasBody(): Boolean {
        return blockList[blockIndex].getNameOfBlock() in listOf(
            CallFunctionBlock.BLOCK_NAME,
            ElseBlock.BLOCK_NAME,
            IfBlock.BLOCK_NAME,
            BLOCK_NAME
        )
    }

    override val blockName = BLOCK_NAME
    lateinit var funList: List<FunctionClass>

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
        while (conditionState.value == "true") {
            // Выполнение тела while
            while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME &&
                blockIndex <= blockList.size - 1
            ) {
                // Выход из цикла при выполнении Return блока
                if (blockList[blockIndex].getNameOfBlock() == ReturnBlock.BLOCK_NAME) {
                    blockList[blockIndex].runCodeBlock()
                    return
                }

                // Выполнение остальных блоков
                if (hasBody()) {
                    (blockList[blockIndex] as HasBodyBlock).setFunctionList(funList)
                }

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

    // Передача списка доступных функций
    override fun setFunctionList(functionList: List<FunctionClass>) {
        funList = functionList
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