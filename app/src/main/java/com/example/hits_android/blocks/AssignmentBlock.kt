package com.example.hits_android.blocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.Type
import com.example.hits_android.expressionParser.variables

// Блок присвоения переменной нового значения
class AssignmentBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Assign",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "assignmentBlock"
    }

    override val blockName = BLOCK_NAME

    var variableName: String = "" // Название изменяемой переменной
    var newValue: String = ""     // Новое значение изменяемой переменной

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Присваивание переменной нового значения
    override fun runCodeBlock() {
        // Индекс и название массива
        var arrayIndex = -1
        var arrName = ""

        // Нахождение индекса массива
        if ('[' in variableName) {
            val index =
                variableName.slice((variableName.indexOf('[') + 1)..(variableName.indexOf(']') - 1))
            val indexExpression =
                ParsingFunctions(LexicalComponents(index + ";").getTokensFromCode())

            arrayIndex = (indexExpression).parseExpression()!!.value.toString().toInt()
            arrName = variableName.slice(0..variableName.indexOf('[') - 1)
        }

        // Проверка существования переменной
        if (variables[variableName] == null && variables[arrName] == null) {
            throw Exception("Присваивание к несуществующей переменной")
        }

        // Присвоение значения переменной типа Int
        if (arrayIndex == -1) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            variables[variableName] = expression.parseExpression()!!
        }

        // Присвоение значения элементу массива
//        else if ((variables[arrName]?.value as Array<*>)[0] is Int) {
        else if (variables[arrName]?.type == Type.INT) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<Int>)[arrayIndex] =
                expression.parseExpression()!!.value.toString().toInt()
        } else if ((variables[arrName]?.value as Array<*>)[0] is Double) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<Double>)[arrayIndex] =
                expression.parseExpression()!!.value.toString().toDouble()
        } else if (variables[arrName]?.type == Type.STRING) {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            (variables[arrName]?.value as Array<String>)[arrayIndex] =
                expression.parseExpression()!!.value.toString()
        } else {
            val expression = ParsingFunctions(LexicalComponents(newValue).getTokensFromCode())
            val result = expression.parseExpression()!!.value.toString()

            if (result == "0" || result == "false") {
                (variables[arrName]?.value as Array<String>)[arrayIndex] = "0"
            } else {
                (variables[arrName]?.value as Array<String>)[arrayIndex] = "1"
            }
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блоков без UI
    fun testBlock(oldVal: String, newVal: String) {
        variableName = oldVal
        newValue = newVal
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        item as AssignmentBlock
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(70.dp)
                .padding(start = calculatePadding(codeBlocksList, item.key))
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ItemLeftOperand(item)
                Text(
                    textAlign = TextAlign.Center,
                    text = "=",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                ItemRightOperand(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemLeftOperand(item: AssignmentBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.variableName)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.variableName = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(0.43f),
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "left operand") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.variableName = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemRightOperand(item: AssignmentBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.newValue)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.newValue = textState.value.text
            },
            placeholder = { Text(text = "right operand") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.newValue = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}