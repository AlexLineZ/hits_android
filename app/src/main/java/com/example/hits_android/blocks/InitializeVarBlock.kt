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
import androidx.compose.material3.DropdownMenu
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
import com.example.hits_android.expressionParser.*

// Блок создания новой переменной типа Int
class InitializeVarBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "InitVar",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initVarBlock"
    }

    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var name: String = ""  // Название переменной
    var type: String = "Int"  // Тип переменной
    var value: String = "" // Значение переменной

    // Создание новой переменной
    override fun runCodeBlock() {
        if (!(Regex("^(?!true|false|\\d)\\w+").matches(name))) {
            throw Exception("Некорректное название переменной")
        }

        // Пересоздание переменной
        if (variables[name] != null) {
            throw Exception("Происходит пересоздание переменной");
        }

        // Вычисление значения переменной
        val expression = ParsingFunctions(LexicalComponents(value + ";").getTokensFromCode())
        val newVariable = expression.parseExpression()!!

        if (type != newVariable.type &&
            !(type == Type.DOUBLE && newVariable.type == Type.INT) &&
            !(type == Type.INT && newVariable.type == Type.DOUBLE)
        ) {
            throw Exception("Переменной типа ${type} присваивается значение типа ${newVariable.type}")
        }

        // Обрезка дробной части у переменной типа Int
        if (type == Type.INT && newVariable.value.toString().contains(".")) {
            newVariable.value = newVariable.value.toString()
                .slice(0..newVariable.value.toString().indexOf('.') - 1)
        }

        // Перевод bool значения в 1 или 0
        if (type == Type.BOOL) {
            if (newVariable.value.toString() != "0" && newVariable.value.toString() != "false") {
                newVariable.value = "1"
            } else {
                newVariable.value = "0"
            }
        }

        // Обрезание "" у строк
        if (type == Type.STRING) {
            newVariable.value = newVariable.value.toString()
        }

        // Сохранение переменной
        variables[name] = newVariable

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блоков без UI
    fun testBlock(n: String, t: String, v: String) {
        name = n
        type = t
        value = v
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        item as InitializeVarBlock
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
                Text(
                    textAlign = TextAlign.Center,
                    text = "Var",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                DropdownMenu(item)
                ItemNameField(item)
                ItemValueField(item)
            }
        }
    }

    @Composable
    fun DropdownMenu(item: InitializeVarBlock) {
        val types = listOf("Int", "Double", "Bool", "String")
        val selectedType = remember { mutableStateOf<String?>(null) }
        val expanded = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .fillMaxHeight()
                .clickable(onClick = { expanded.value = true }),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = selectedType.value ?: type,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                textDecoration = TextDecoration.Underline
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        selectedType.value = type
                        item.type = type
                        expanded.value = false
                    },
                    text = { Text(text = type) }
                )
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemNameField(item: InitializeVarBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.name)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.name = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.name = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemValueField(item: InitializeVarBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.value)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.value = textState.value.text
            },
            placeholder = { Text(text = "value") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.value = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}