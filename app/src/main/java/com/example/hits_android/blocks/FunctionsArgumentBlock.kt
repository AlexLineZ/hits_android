package com.example.hits_android.blocks

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.Type
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.expressionParser.variables

class FunctionsArgumentBlock(
    override var key: String,
    override val title: String = "Args",
    override val isDragOverLocked: Boolean = false,
) : Block {

    // Название блока
    companion object {
        const val BLOCK_NAME = "functionsArgumentBlock"
    }

    override val blockName = BLOCK_NAME

    var parameters: String = ""
    var varList = mutableListOf<Variable>()
    var parametersList = mutableListOf<Variable>()

    // Проверка приводимости типов данных
    private fun isNotComparable(first: Variable, second: Variable): Boolean {
        return first.type != second.type &&
                !(first.type == Type.INT && second.type == Type.DOUBLE) &&
                !(first.type == Type.DOUBLE && second.type == Type.INT) &&
                !(first.type == Type.STRING && second.type == Type.CHAR) &&
                !(first.type == Type.CHAR && second.type == Type.INT)
    }

    // Выполнение блока FunctionNameBlock
    override fun runCodeBlock() {
        val parList = parameters.split(",").toMutableList()

        if (parameters == "") {
            parList.removeAt(0)
        }

        if (parametersList.size == 0) {
            // Инициализация параметров
            for (i in parList.indices) {
                parList[i] = parList[i].replace(" ", "")

                val expression = LexicalComponents(parList[i]).getTokensFromCode()

                if (expression.size != 3) {
                    throw Exception("Incorrect specification of function parameters")
                }

                val name = expression[0].text
                val type = expression[2].text

                parametersList.add(Variable(name, type, ""))
            }
        }
        // Проверка соответствия кол-ва аргументов и параметров
        if (parametersList.size != varList.size) {
            throw Exception("The number of arguments when calling a function does not match the number of its parameters")
        }

        // Проверка соответствия типов аргументов и параметров
        for (j in varList.indices) {
            if (isNotComparable(parametersList[j], varList[j])) {
                throw Exception("Argument and parameter type mismatch")
            }
        }

        // Принятие аргументов функцией
        for (k in varList.indices) {
            if (parametersList[k].type == Type.STRING && varList[k].type == Type.CHAR) {
                variables[parametersList[k].name] = Variable(
                    parametersList[k].name,
                    parametersList[k].type,
                    varList[k].value
                )
            } else if (parametersList[k].type == Type.CHAR && varList[k].type == Type.INT) {
                variables[parametersList[k].name] = Variable(
                    parametersList[k].name,
                    parametersList[k].type,
                    varList[k].value.toString().toInt().toChar()
                )
            } else {
                variables[parametersList[k].name] = Variable(
                    parametersList[k].name,
                    parametersList[k].type,
                    varList[k].value
                )
            }
        }

        blockIndex++

        parametersList.clear()
        varList.clear()
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    // Передача аргументов функции
    fun setArguments(args: MutableList<Variable>) {
        varList = args
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as FunctionsArgumentBlock
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(70.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                ItemConditionField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemConditionField(item: FunctionsArgumentBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.parameters)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.parameters = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "a: Int, b: StringArray", maxLines = 1) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.parameters = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}