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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.Type
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables

// Блок создания массива
class InitializeArrayBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
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
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = calculatePadding(codeBlocksList, item.key))
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}