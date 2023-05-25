package com.example.hits_android.blocks

import androidx.compose.foundation.background
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
import com.example.hits_android.expressionParser.*
import kotlin.math.roundToInt

// Блок присвоения переменной нового значения
class AssignmentBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override var key: String,
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

    // Проверка возможности перевода типов переменных
    private fun isNotComparableType(newVariable: Variable): Boolean {
        return variables[variableName]?.type != newVariable.type &&
                !(variables[variableName]?.type == Type.DOUBLE && newVariable.type == Type.INT) &&
                !(variables[variableName]?.type == Type.INT && newVariable.type == Type.DOUBLE) &&
                !(variables[variableName]?.type == Type.STRING && newVariable.type == Type.CHAR) &&
                !(variables[variableName]?.type == Type.CHAR && newVariable.type == Type.INT)
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
        if (isNotComparableType(newVariable)) {
            throw Exception("Переменной типа ${variables[variableName]?.type} присваивается значение типа ${newVariable.type}")
        }

        // Присвоение переменной типа Char значения типа Int
        if (variables[variableName]?.type == Type.CHAR && newVariable.type == Type.INT) {
            variables[variableName] = Variable(newVariable.name, Type.CHAR, newVariable.value.toString().toInt().toChar().toString())
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
            throw Exception("Некорректное обращение к полю структуры.")
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
            val currentField = (variables[structName]?.value as MutableMap<String, Variable>)[fieldName]
            // Проверка соответсвтия типов переменной и значения
//            if (isNotComparableType(newVariable)) {
//                throw Exception("Переменной типа ${variables[variableName]?.type} присваивается значение типа ${newVariable.type}")
//            }

            // Присвоение переменной типа Char значения типа Int
            if (currentField?.type == Type.CHAR && newVariable.type == Type.INT) {
                (variables[structName]?.value as MutableMap<String, Variable>)[fieldName] = Variable(
                    newVariable.name,
                    Type.CHAR,
                    newVariable.value.toString().toInt().toChar().toString()
                )
            }
            // Остальные присвоения
            else {
                (variables[structName]?.value as MutableMap<String, Variable>)[fieldName] = newVariable
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
            throw Exception("Переменной типа" +
                    " ${variables[arrName]?.type?.slice(0..(variables[arrName]?.type!!.length-6))}" +
                    " присваивается значение типа ${newVariable.type}")
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
                }
                else {
                    (variables[arrName]?.value as Array<String>)[arrayIndex] =
                        newVariable.value.toString().toInt().toChar().toString()
                }
            }

            // Символу из строки
            Type.STRING -> {
                val str = variables[arrName]?.value as String
                variables[arrName] = Variable(arrName, Type.STRING,
                    str.substring(0, arrayIndex) + newVariable.value.toString() + str.substring(arrayIndex + 1))
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
                throw Exception("Некорректный индекс массива")
            }

            arrayIndex = indexVariable.value.toString().toInt()
            arrName = variableName.slice(0..variableName.indexOf('[') - 1)
        }

        // Проверка существования переменной
        if (variables[variableName] == null && variables[arrName] == null &&
                !variableName.contains('.')) {
            throw Exception("Присваивание к несуществующей переменной")
        }

        // Присвоение значения переменной
        if (arrayIndex == -1 && !variableName.contains('.')) {
            assignVariable()
        }
        // Присвоение значения объекту
        else if (arrayIndex == -1 && variableName.contains('.')){
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
        item as AssignmentBlock
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
            placeholder = { Text(text = "left operand", maxLines = 1) },
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
            placeholder = { Text(text = "right operand", maxLines = 1) },
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