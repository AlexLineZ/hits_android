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

// Блок условия
class IfBlock(
    override var key: String,
    override val title: String = "If",
    override val isDragOverLocked: Boolean = false,
    val beginKey: String = "",
    val endKey: String = ""
) : Block, HasBodyBlock {
    // Название блока
    companion object {
        const val BLOCK_NAME = "IfBlock"
    }

    override val blockName = BLOCK_NAME
    lateinit var funList: List<FunctionClass>

    // Проверка прерывания выполнения блока If
    private fun isBreaking():Boolean {
        return blockList[blockIndex].getNameOfBlock() == BreakBlock.BLOCK_NAME ||
                blockList[blockIndex].getNameOfBlock() == ContinueBlock.BLOCK_NAME ||
                blockList[blockIndex].getNameOfBlock() == ReturnBlock.BLOCK_NAME
    }

    // Условие
    var condition: String = ""

    // Выполнение блока If
    override fun runCodeBlock() {
        // Выполнение следующих блоков
        blockIndex++

        // Проверка условия
        val conditionExpression =
            ParsingFunctions(LexicalComponents(condition + ";").getTokensFromCode())
        val conditionState = conditionExpression.parseExpression()!!

        // Если условие верно
        if (conditionState.value == "1") {
            // Выполнение тела if
            while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
                // Выход из условия при выполнении блоков Break, Сontinue или Return
                if (isBreaking()) {
                    blockList[blockIndex].runCodeBlock()
                    return
                }

                if (blockList[blockIndex].getNameOfBlock() == CallFunctionBlock.BLOCK_NAME) {
                    (blockList[blockIndex] as CallFunctionBlock).setFunctionList(funList)
                }

                // Выполнение остальных блоков
                blockList[blockIndex].runCodeBlock()
            }

            // Удаление переменных, которые были созданы внутри тела if
            blockList[blockIndex].runCodeBlock()

            // Пропуск else
            if (blockIndex < blockList.size && blockList[blockIndex].getNameOfBlock() == ElseBlock.BLOCK_NAME) {
                blockIndex++
                skipBlock()
            }
        }
        // Пропуск тела if, если условие неверно
        else {
            skipBlock()
        }
    }

    // Возвращение названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    // Передача списка доступных функций
    override fun setFunctionList(functionList:  List<FunctionClass>) {
        funList = functionList
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as IfBlock
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                ItemConditionField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemConditionField(item: IfBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.condition)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.condition = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "condition", maxLines = 1) },
            shape = RoundedCornerShape(6.dp),
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