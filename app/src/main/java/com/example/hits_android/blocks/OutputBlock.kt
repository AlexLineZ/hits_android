package com.example.hits_android.blocks

import androidx.compose.foundation.background
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
import com.example.hits_android.expressionParser.Type
import com.example.hits_android.expressionParser.Variable
import com.example.hits_android.model.FlowViewModel

// Блок вывода
class OutputBlock(
    override var previousID: Int = -1,
    override var nextID: Int = -1,
    override val key: String,
    override val title: String = "Print",
    override val isDragOverLocked: Boolean = false
) : Block {
    // Название блока
    companion object {
        val BLOCK_NAME = "outputBlock"
    }

    override val blockName = BLOCK_NAME

    // Выражение, переданное блоку в качестве параметра
    var expression: String = ""

    // Добавление блока в список блоков
    init {
        blockList.add(this)
    }

    // Вывод в консоль
    override fun runCodeBlock() {
        val exp = ParsingFunctions(LexicalComponents(expression + ";").getTokensFromCode())
        var result = exp.parseExpression()

        if (result!!.type.length >= 5 &&
            result!!.type.slice((result!!.type.length - 5)..(result!!.type.length - 1)) == "Array") {
            var arr = ""

            for (i in (result.value as Array<*>).indices) {
                arr += (result.value as Array<*>)[i].toString() + ", "
            }

            arr = arr.slice(0..arr.length - 3)
            result = Variable("result", Type.STRING, arr)
        }

        when (result) {
            null -> println("ඞ Empty")
            else -> {
                FlowViewModel().setCurrentValue("ඞ ${result.value}\n")
            }
        }

        // Выполнение следующего блока
        blockIndex++
    }

    // Тестирование блока без UI
    fun testBlock(exp: String) {
        expression = exp
    }

    // Возврат названия блока
    override fun getNameOfBlock(): String {
        return blockName
    }

    @Composable
    override fun BlockComposable(item: Block, codeBlocksList: List<Block>) {
        item as OutputBlock
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                ItemConditionField(item)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ItemConditionField(item: OutputBlock) {
        val textState = remember { mutableStateOf(TextFieldValue(text = item.expression)) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                item.expression = textState.value.text
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "something") },
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // Изменяем действие клавиатуры на "Готово"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    item.expression = textState.value.text
                    keyboardController?.hide() // Скрываем клавиатуру
                }
            )
        )
    }
}
