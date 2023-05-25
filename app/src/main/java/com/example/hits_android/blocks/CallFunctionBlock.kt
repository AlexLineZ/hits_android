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
import com.example.hits_android.expressionParser.LexicalComponents
import com.example.hits_android.expressionParser.ParsingFunctions
import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

// Блок вызова функции
class CallFunctionBlock(
    override val key: String,
    override val title: String = "CallFun",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "callFunctionBlock"
    }

    override val blockName = BLOCK_NAME

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    var functionName = "" // Название вызываемой функции
    var arguments = ""    // Передаваемые аргументы

    // Вызов функции
    override fun runCodeBlock() {
        println(arguments)

        // Запоминание места вызова функции
        val callingIndex = blockIndex

        // Поиск определения функции
        while (blockList[blockIndex].getNameOfBlock() != FunctionBlock.BLOCK_NAME ||
            (blockList[blockIndex].getNameOfBlock() == FunctionBlock.BLOCK_NAME &&
                    (blockList[blockIndex] as FunctionBlock).getFunctionName() != functionName)
        ) {
            try {
                blockIndex--

                if (blockList[blockIndex].getNameOfBlock() == BeginBlock.BLOCK_NAME) {
                    scopes.destroyScope()
                } else if (blockList[blockIndex].getNameOfBlock() == EndBlock.BLOCK_NAME) {
                    scopes.addScope(scopes.getScope())
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw Exception("Функция ${functionName} не была найдена при вызове.")
            }
        }

        // Переход к области видимости функции
        scopes.addScope((blockList[blockIndex] as FunctionBlock).getScope())

        // Передача аргументов
        val argList = arguments.split(",").toMutableList()

        if (arguments == "") {
            argList.removeAt(0)
        }

        if (argList.size != (blockList[blockIndex] as FunctionBlock).getParameters().size) {
            throw Exception(
                "Кол-во аргументов при вызове функции ${functionName} не совпадает с кол-вом" +
                        " её параметров."
            )
        }

        for (i in argList.indices) {
            //argList[i] = argList[i].replace(" ", "")
            argList[i] += ";"

            val expression = ParsingFunctions(LexicalComponents(argList[i]).getTokensFromCode())
            val args = expression.parseExpression()!!

            if (variables[(blockList[blockIndex] as FunctionBlock).getParameters()[i]]?.type != args.type) {
                throw Exception("При вызове функции ${functionName} переданы аргументы неподходящих типов.")
            }

            variables[(blockList[blockIndex] as FunctionBlock).getParameters()[i]]?.value =
                args.value
        }

        // Выполнение тела функции
        blockIndex += 1

        while (blockList[blockIndex].getNameOfBlock() != EndBlock.BLOCK_NAME) {
            blockList[blockIndex].runCodeBlock()
        }

        // Удаление переменных, которые были созданы внутри тела if
        blockList[blockIndex].runCodeBlock()

        // Возврат на место вызова функции
        blockIndex = callingIndex + 1
    }

    // Тестирование без UI
    fun testBlock(funName: String, args: String) {
        functionName = funName
        arguments = args
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
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
        val textState = remember { mutableStateOf(TextFieldValue(text = item.functionName)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.functionName = textState.value.text
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
                    item.functionName = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}