package com.example.hits_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.model.FlowViewModel
import com.example.hits_android.model.isTextFieldVisible

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Console(viewModel: FlowViewModel) {
    val output by viewModel.output.collectAsState()
    val textFieldVisibleState = isTextFieldVisible.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val textState = remember { mutableStateOf(TextFieldValue(text = "")) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.92f)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Console",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
        item {
            Text(
                text = output,
                modifier = Modifier.padding(15.dp)
            )
            if (textFieldVisibleState.value) {
                TextField(
                    value = textState.value,
                    onValueChange = {
                        textState.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            FlowViewModel().setInput(textState.value.text)
                            FlowViewModel().setCurrentValue(">> ${textState.value.text}\n")
                            textState.value = TextFieldValue(text = "")
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }
    }

}