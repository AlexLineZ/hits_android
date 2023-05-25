package com.example.hits_android.blocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables

// Блок создания массива
class InitializeArrayBlock(
    override val key: String,
    override val title: String = "InitArray",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "initArrayBlock"
    }

    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var arrayName = ""      // Название массива
    var arrayType = "Int"   // Тип элементов массива
    var arraySize = ""      // Размер массива

    // Создание массива
    override fun runCodeBlock() {
        if (!(Regex("^(?!true|false|\\d)\\w+").matches(arrayName))) {
            throw Exception("Некорректное название массива")
        }

        // Пересоздание массива
        if (variables[arrayName] != null) {
            throw Exception("Происходит пересоздание переменной")
        }

        // Проверка размера массива
        val expression = ParsingFunctions(LexicalComponents(arraySize + ";").getTokensFromCode())
        val size = expression.parseExpression()!!

        if (size.type != Type.INT || size < Variable("", Type.INT, "0")) {
            throw Exception("Некорректный размер массива")
        }

        // Создание массива с элементами типа Int
        if (arrayType == Type.INT) {
            variables[arrayName] = Variable(
                arrayName,
                Type.INT + "Array",
                Array(size.value.toString().toInt()) { 0 })
        }

        // Создание массива с элементами типа Double
        else if (arrayType == Type.DOUBLE) {
            variables[arrayName] = Variable(
                arrayName,
                Type.DOUBLE + "Array",
                Array(size.value.toString().toInt()) { 0.0 })
        }

        // Создание массива с элементами типа String
        else if (arrayType == Type.STRING) {
            variables[arrayName] = Variable(
                arrayName,
                Type.STRING + "Array",
                Array(size.value.toString().toInt()) { "" })
        }

        // Создание массива с элементами типа Bool
        else if (arrayType == Type.BOOL) {
            variables[arrayName] = Variable(
                arrayName,
                Type.BOOL + "Array",
                Array(size.value.toString().toInt()) { "0" })
        }

        // Создание массива с элементами типа Char
        else if (arrayType == Type.CHAR) {
            variables[arrayName] = Variable(
                arrayName,
                Type.CHAR + "Array",
                Array(size.value.toString().toInt()) {""}
            )
        }

        // Выполнение следующих блоков
        blockIndex++
    }

    // Тестирование без UI
    fun testBlock(name: String, type: String, size: String) {
        arrayName = name
        arrayType = type
        arraySize = size
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as InitializeArrayBlock
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Arr",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                DropdownMenu(item)
                ItemNameField(item)
                ItemSizeField(item)
            }
        }
    }

    @Composable
    fun DropdownMenu(item: InitializeArrayBlock) {
        val types = listOf("Int", "Double", "Bool", "String", "Char")
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
                text = selectedType.value ?: arrayType,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline
            )
        }
        androidx.compose.material3.DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        selectedType.value = type
                        item.arrayType = type
                        expanded.value = false
                    },
                    text = { Text(text = type) }
                )
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemNameField(item: InitializeArrayBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.arrayName)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.arrayName = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "name", maxLines = 1) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.arrayName = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemSizeField(item: InitializeArrayBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.arraySize)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.arraySize = textState.value.text
            },
            placeholder = { Text(text = "size", maxLines = 1) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.arraySize = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}