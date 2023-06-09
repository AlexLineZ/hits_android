package com.example.hits_android.blocks

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.expressionParser.*

// Блок вызова функции
class CallFunctionBlock(
    override var key: String,
    override val title: String = "CallFun",
    override val isDragOverLocked: Boolean = false
) : Block, HasBodyBlock {
    // Название блока
    companion object {
        const val BLOCK_NAME = "callFunctionBlock"
    }

    override val blockName = BLOCK_NAME

    var functionName = "" // Название вызываемой функции
    var arguments = ""    // Передаваемые аргументы
    lateinit var funList: List<FunctionClass>

    // Вызов функции
    override fun runCodeBlock() {
        // Проверка названия  переменной
        if (!(Regex("^(?!true|false|\\d)\\w+").matches(functionName))) {
            throw Exception("Некорректное название функции")
        }

        // Чтение переданных аргументов
        val argList = arguments.split(",").toMutableList()
        val varList = mutableListOf<Variable>()

        if (arguments == "") {
            argList.removeAt(0)
        }

        // Передача аргументов
        for (i in argList.indices) {
            argList[i] += ";"
            val expression = ParsingFunctions(LexicalComponents(argList[i]).getTokensFromCode())
            varList.add(expression.parseExpression()!!)
        }

        // Выполнение тела функции
        var count = 0
        var funIndex = 0

        if (functionName == "main") {
            throw Exception("Calling \"main\" function from another function")
        }
        if (functionName == "") {
            throw Exception("Called function name not specified")
        }

        for (j in 1..funList.size - 1) {
            if (funList[j].getName() == functionName) {
                count++
                funIndex = j
            }
        }

        if (count > 1) {
            throw Exception("Undefined function call ${functionName}")
        } else if (count == 0) {
            throw Exception("Функция ${functionName} не найдена")
        }

        val savedList = blockList
        val savedIndex = blockIndex
        val savedVariables = variables.toMap()
        funList[funIndex].setArguments(varList)
        funList[funIndex].runFunction(funList)

        blockList = savedList
        blockIndex = savedIndex
        variables = savedVariables.toMutableMap()

        blockIndex++
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    // Передача списка функций
    override fun setFunctionList(functionList: List<FunctionClass>) {
        funList = functionList
    }

    @Composable
    override fun BlockComposable(item: Block) {
        item as CallFunctionBlock
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
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Fun",
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                ItemFunNameField(item)
                ItemFunArgsField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemFunNameField(item: CallFunctionBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.functionName)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.functionName = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(0.3f),
            placeholder = { Text(text = "name", maxLines = 1) },
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.functionName = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemFunArgsField(item: CallFunctionBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.arguments)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.arguments = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "arg1,  arg2,  arg3", maxLines = 1) },
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.arguments = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}