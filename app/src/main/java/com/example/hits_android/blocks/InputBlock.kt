package com.example.hits_android.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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
import com.example.hits_android.model.newInput
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class InputBlock(
    override var key: String,
    override val title: String = "Input",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "inputBlock"
    }

    override val blockName = BLOCK_NAME

    var variableName: String = "" // Название изменяемой переменной
    var newValue: String = ""     // Новое значение изменяемой переменной

    // Проверка возможности перевода типов переменных
    private fun isNotComparableType(oldVariabel: Variable?, newVariable: Variable): Boolean {
        return oldVariabel?.type != newVariable.type &&
                !(oldVariabel?.type == Type.DOUBLE && newVariable.type == Type.INT) &&
                !(oldVariabel?.type == Type.INT && newVariable.type == Type.DOUBLE) &&
                !(oldVariabel?.type == Type.STRING && newVariable.type == Type.CHAR) &&
                !(oldVariabel?.type == Type.CHAR && newVariable.type == Type.INT)
    }

    // Проверка возможности записать в массив новое значение элемента
    private fun isNotComparableArrType(newVariable: Variable, arrName: String): Boolean {
        return variables[arrName]?.type != newVariable.type + "Array" &&
                !(variables[arrName]?.type == Type.DOUBLE + "Array" && newVariable.type == Type.INT) &&
                !(variables[arrName]?.type == Type.INT + "Array" && newVariable.type == Type.DOUBLE) &&
                !(variables[arrName]?.type == Type.STRING + "Array" && newVariable.type == Type.CHAR) &&
                !(variables[arrName]?.type == Type.CHAR + "Array" && newVariable.type == Type.INT) &&
                !(newVariable.type == Type.CHAR && variables[arrName]?.type == Type.STRING)
    }

    // Присвоение значения переменной
    private fun assignVariable() {
        val expression = ParsingFunctions(LexicalComponents(newValue + ";").getTokensFromCode())
        val newVariable = expression.parseExpression()!!

        // Проверка соответсвтия типов переменной и значения
        if (isNotComparableType(variables[variableName], newVariable)) {
            throw Exception("A variable of type ${variables[variableName]?.type} is assigned a value of type ${newVariable.type}")
        }

        // Присвоение переменной типа Char значения типа Int
        if (variables[variableName]?.type == Type.CHAR && newVariable.type == Type.INT) {
            variables[variableName] = Variable(
                newVariable.name,
                Type.CHAR,
                newVariable.value.toString().toInt().toChar().toString()
            )
        }

        // Остальные присвоения
        else {
            variables[variableName] = newVariable
        }
    }

    // Присвоение значения объекту
    private fun assignStruct(name: String) {
        val expression = ParsingFunctions(LexicalComponents(newValue + ";").getTokensFromCode())
        val newVariable = expression.parseExpression()!!

        if (name.split('.').count() != 2) {
            throw Exception("Incorrect access to structure field ${name}")
        }

        if (newVariable.type == Type.STRUCT) {
            throw Exception("Assigning a structure field to another structure")
        }

        val structName = name.split('.')[0]
        val fieldName = name.split('.')[1]

        // Проверка существования поля
        var isNewField = true

        for (key in (variables[structName]?.value as MutableMap<String, Variable>).keys) {
            if (fieldName == key) {
                isNewField = false
                break
            }
        }

        // Создание нового поля
        if (isNewField) {
            (variables[structName]?.value as MutableMap<String, Variable>)[fieldName] = newVariable
        }

        // Присвоение значения уже существующему полю
        else {
            val currentField =
                (variables[structName]?.value as MutableMap<String, Variable>)[fieldName]
            // Проверка соответсвтия типов переменной и значения
            if (isNotComparableType(currentField, newVariable)) {
                throw Exception("A variable of type ${currentField?.type} is assigned a value of type ${newVariable.type}")
            }

            // Присвоение переменной типа Char значения типа Int
            if (currentField?.type == Type.CHAR && newVariable.type == Type.INT) {
                (variables[structName]?.value as MutableMap<String, Variable>)[fieldName] =
                    Variable(
                        newVariable.name,
                        Type.CHAR,
                        newVariable.value.toString().toInt().toChar().toString()
                    )
            }
            // Остальные присвоения
            else {
                (variables[structName]?.value as MutableMap<String, Variable>)[fieldName] =
                    newVariable
            }
        }
    }

    // Присвоение значения элементу массива
    private fun assignArray(arrName: String, arrayIndex: Int) {
        // Нахождение нового значения
        val expression = ParsingFunctions(LexicalComponents(newValue + ";").getTokensFromCode())
        val newVariable = expression.parseExpression()!!

        // Проверка соответствия типов
        if (isNotComparableArrType(newVariable, arrName)) {
            throw Exception(
                "To the variable of type" +
                        " ${variables[arrName]?.type?.slice(0..(variables[arrName]?.type!!.length - 6))}" +
                        "is assigned the value of type ${newVariable.type}"
            )
        }

        // Присвоение нового значения
        when (variables[arrName]?.type) {
            // Элементу Int массива
            Type.INT + "Array" -> (variables[arrName]?.value as Array<Int>)[arrayIndex] =
                newVariable.value.toString().toDouble().roundToInt()

            // Элементу Double массива
            Type.DOUBLE + "Array" -> (variables[arrName]?.value as Array<Double>)[arrayIndex] =
                newVariable.value.toString().toDouble()

            // Элементу Char массива
            Type.CHAR + "Array" -> {
                if (newVariable.type != Type.INT) {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] =
                        newVariable.value.toString()
                } else {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] =
                        newVariable.value.toString().toInt().toChar().toString()
                }
            }

            // Символу из строки
            Type.STRING -> {
                val str = variables[arrName]?.value as String
                variables[arrName] = Variable(
                    arrName, Type.STRING,
                    str.substring(0, arrayIndex) + newVariable.value.toString() + str.substring(
                        arrayIndex + 1
                    )
                )
            }

            // Элементу String массива
            Type.STRING + "Array" -> (variables[arrName]?.value as Array<String>)[arrayIndex] =
                newVariable.value.toString()

            // Элементу Bool массива
            Type.BOOL + "Array" -> {
                val result = newVariable.value.toString()

                if (result == "0" || result == "false") {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] = "0"
                } else {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] = "1"
                }
            }

            // Копирование массивов
            else -> variables[arrName] = newVariable
        }
    }


    // Присваивание переменной нового значения
    override fun runCodeBlock() {
        newValue = ""
        FlowViewModel().clearInput()
        // Индекс и название массива
        var arrayIndex = -1
        var arrName = ""

        // Нахождение индекса массива
        if ('[' in variableName) {
            val index =
                variableName.slice((variableName.indexOf('[') + 1)..(variableName.indexOf(']') - 1))
            val indexExpression =
                ParsingFunctions(LexicalComponents(index + ";").getTokensFromCode())
            val indexVariable = (indexExpression).parseExpression()!!

            if (indexVariable.type != Type.INT || indexVariable < Variable("", Type.INT, 0)) {
                throw Exception("Incorrect array index")
            }

            arrayIndex = indexVariable.value.toString().toInt()
            arrName = variableName.slice(0..variableName.indexOf('[') - 1)
        }

        // Проверка существования переменной
        if (variables[variableName] == null && variables[arrName] == null &&
            !variableName.contains('.')
        ) {
            throw Exception("Entering a non-existent variable ${variableName}")
        }

        FlowViewModel().changeVisibilityTextField()
        while (newValue == "") {
            val input: StateFlow<String> = newInput.asStateFlow()
            newValue = input.value
        }
        FlowViewModel().changeVisibilityTextField()


        // Присвоение значения переменной
        if (arrayIndex == -1 && !variableName.contains('.')) {
            assignVariable()
        }
        // Присвоение значения объекту
        else if (arrayIndex == -1 && variableName.contains('.')) {
            assignStruct(variableName)
        }
        // Присвоение значения элементу массива
        else {
            assignArray(arrName, arrayIndex)
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as InputBlock
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
                ItemLeftOperand(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemLeftOperand(item: InputBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.variableName)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.variableName = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "variable", maxLines = 1) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.variableName = textState.value.text
                    keyboardController?.hide()
                }
            )
        )
    }
}