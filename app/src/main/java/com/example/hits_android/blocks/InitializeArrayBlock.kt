package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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

    var arrayName = ""   // Название массива
    var arrayType = ""   // Тип элементов массива
    var arraySize = ""   // Размер массива

    // Создание массива
    override fun runCodeBlock() {
        // Пересоздание массива
        if (variables[arrayName] != null) {
            throw Exception("Происходит пересоздание переменной")
        }

        // Создание массива с элементами типа Int
        if (arrayType == Type.INT) {
            val expression = ParsingFunctions(LexicalComponents(arraySize).getTokensFromCode())
            variables[arrayName] = Variable(
                arrayName,
                "ArrayInt",
                Array(expression.parseExpression()!!.value.toString().toInt()) { 0 })
        }

        // Создание массива с элементами типа Double
        else if (arrayType == Type.DOUBLE) {
            val expression = ParsingFunctions(LexicalComponents(arraySize).getTokensFromCode())
            variables[arrayName] = Variable(
                arrayName,
                "ArrayDouble",
                Array(expression.parseExpression()!!.value.toString().toInt()) { 0.0 })
        }

        // Создание массива с элементами типа String
        else if (arrayType == Type.STRING) {
            val expression = ParsingFunctions(LexicalComponents(arraySize).getTokensFromCode())
            variables[arrayName] = Variable(
                arrayName,
                "ArrayString",
                Array(expression.parseExpression()!!.value.toString().toInt()) { "" })
        }

        // Создание массива с элементами типа Bool
        else if (arrayType == Type.BOOL) {
            val expression = ParsingFunctions(LexicalComponents(arraySize).getTokensFromCode())
            variables[arrayName] = Variable(
                arrayName,
                "ArrayBool",
                Array(expression.parseExpression()!!.value.toString().toInt()) { "0" })
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