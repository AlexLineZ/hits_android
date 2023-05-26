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
import com.example.hits_android.expressionParser.*
import com.example.hits_android.model.FlowViewModel

// Блок вывода
class OutputBlock(
    override var key: String,
    override val title: String = "Print",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "outputBlock"
    }

    override val blockName = BLOCK_NAME

    // Выражение, переданное блоку в качестве параметра
    var expression: String = ""

    // Форматирование выывода массива
    private fun parseArray(currentVar: Variable?): String {
        var arr = ""

        // Разделение элементов массивов запятыми
        for (i in (currentVar?.value as Array<*>).indices) {
            if (currentVar?.type == Type.STRUCT + "Array") {
                arr += parseStruct(Variable("", Type.STRUCT,
                    (currentVar.value as Array<MutableMap<String, Variable>>)[i])) + ", "
            }
            else {
                arr += (currentVar.value as Array<*>)[i].toString() + ", "
            }
        }

        // Помещение элементов массивов в фигурные скобки
        arr = arr.slice(0..arr.length - 3)
        return "{" + arr + "}"
    }

    // Форматирование вывода структуры
    private fun parseStruct(currentVar: Variable): String {
        var text = "{"
        val struct = currentVar.value as MutableMap<String, Variable>

        if (struct.keys.size == 0) {
            return "{}"
        }

        for (key in struct.keys) {
            if (struct[key]!!.type.length >= 5 &&
                struct[key]!!.type.slice((struct[key]!!.type.length - 5)..(struct[key]!!.type.length - 1)) == "Array") {
                text += key.toString() + ": " + parseArray(struct[key]) + ", "
            }
            else {
                text += key.toString() + ": " + struct[key]?.value.toString() + ", "
            }
        }

        text = text.slice(0..text.length - 3)

        return text + "}"
    }

    // Вывод в консоль
    override fun runCodeBlock() {
        var outputValue = ""                                // Строка вывода
        val argList = expression.split(",").toMutableList() // Переданные аргументы

        // Проверка наличия выражения
        if (expression == "") {
            throw Exception("В блоке Print нет выражения для вывода")
        }

        // Вывод каждого значения
        for (i in argList.indices) {
            argList[i] += ";"

            // Нахождение текущего значения
            val currentExpression =
                ParsingFunctions(LexicalComponents(argList[i]).getTokensFromCode())
            var currentValue = currentExpression.parseExpression()!!

            // Вывод структуры
            if (currentValue.type == Type.STRUCT) {
                val text = parseStruct(currentValue)
                currentValue = Variable("", Type.STRING, text)
            }

            // Вывод массива
            if (currentValue!!.type.length >= 5 &&
                currentValue!!.type.slice((currentValue!!.type.length - 5)..(currentValue!!.type.length - 1)) == "Array"
            ) {
                currentValue = Variable("currentValue", Type.STRING, parseArray(currentValue))
            }

            // Добавление текущего значения к строке вывода
            outputValue += currentValue.value.toString() + " "
        }

        // Вывод результата
        FlowViewModel().setCurrentValue("ඞ ${outputValue}\n")

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as OutputBlock
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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
    fun ItemConditionField(item: OutputBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.expression)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.expression = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "something") },
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.expression = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}
