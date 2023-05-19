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

        // Присвоение значения переменной
        if (arrayIndex == -1) {
            val expression = ParsingFunctions(LexicalComponents(newValue + ";").getTokensFromCode())
            val newVariable = expression.parseExpression()!!

            // Проверка соответсвтия типов переменной и значения
            if (variables[variableName]?.type != newVariable.type &&
                !(variables[variableName]?.type == Type.DOUBLE && newVariable.type == Type.INT) &&
                !(variables[variableName]?.type == Type.INT && newVariable.type == Type.DOUBLE)
            ) {
                throw Exception("Переменной типа ${variables[variableName]?.type} присваивается значение типа ${newVariable.type}")
            }

            variables[variableName] = newVariable
        }

        // Присвоение значения элементу массива
        else {
            val expression = ParsingFunctions(LexicalComponents(newValue + ";").getTokensFromCode())
            val newVariable = expression.parseExpression()!!

            if (newVariable.type + "Array" != variables[arrName]?.type) {
                throw Exception("Переменной типа" +
                        " ${variables[arrName]?.type?.slice(0..(variables[arrName]?.type!!.length-6))}" +
                        " присваивается значение типа ${newVariable.type}")
            }

            if (variables[arrName]?.type == Type.INT + "Array") {
                (variables[arrName]?.value as Array<Int>)[arrayIndex] =
                    newVariable.value.toString().toInt()
            } else if ((variables[arrName]?.value as Array<*>)[0] is Double) {
                (variables[arrName]?.value as Array<Double>)[arrayIndex] =
                    newVariable.value.toString().toDouble()
            } else if (variables[arrName]?.type == Type.STRING) {
                (variables[arrName]?.value as Array<String>)[arrayIndex] =
                    newVariable.value.toString()
            } else if (variables[arrName]?.type == Type.BOOL + "Array"){
                val result = newVariable.value.toString()

                if (result == "0" || result == "false") {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] = "0"
                } else {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] = "1"
                }
            } else {
                variables[arrName] = newVariable
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