package com.example.hits_android

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hits_android.appmodel.data.filestorage.BlockImpl
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.model.SavesViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun DropdownMenu(svm: SavesViewModel, vm: ReorderListViewModel) {
    val selectedLoad = remember { mutableStateOf<String?>(null) }
    var loadText = "Load"
    val saves = svm.state.collectAsState().value
    val loadedSave = svm.loadedSave.collectAsState().value
    if (loadedSave.name != "---") {
        vm.parseToFunctionList(loadedSave.functionsList)

        Log.d("a", "${loadedSave.functionsList}")
        loadedSave.name = "---"
    }


    val expanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                svm.startGetAllSaves()
                expanded.value = true
            }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = selectedLoad.value ?: loadText,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
    androidx.compose.material3.DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        saves.forEach { name ->
            DropdownMenuItem(
                onClick = {
                    selectedLoad.value = name.realName
                    loadText = name.realName
                    svm.startGetSave(name.name)
                    expanded.value = false
                },
                text = { Text(text = name.realName) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "deleteSave",
                        modifier = Modifier
                            .clickable {
                                svm.startDeleteSave(name.name)
                                expanded.value = false
                            })
                }
            )
        }
    }
}

@Composable
fun SavesBuilderComposable(vm: ReorderListViewModel) {
    val svm: SavesViewModel = koinViewModel()
    var showDialog by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Saves",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(0.5f)
                .clip(shape = RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    showDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Surface(
                        modifier = Modifier.padding(16.dp),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 24.dp,
                        shadowElevation = 24.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Введите значение")
                            TextField(
                                value = enteredText,
                                onValueChange = { enteredText = it },
                                modifier = Modifier.padding(top = 8.dp),
                                singleLine = true
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        showDialog = false
                                        val customList = createFunctionListToJSON(vm.functionsList)
                                        val saveModel = SaveModel(
                                            functionsList = customList,
                                            name = UUID
                                                .randomUUID()
                                                .toString(),
                                            date = System.currentTimeMillis(),
                                            realName = enteredText
                                        )
                                        svm.startWriteSave(saveModel)
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(text = "ОК")
                                }
                            }
                        }
                    }
                }
            }
            Text(
                text = "Save",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            DropdownMenu(svm, vm)
        }
    }
}

fun createFunctionListToJSON(functionList: List<FunctionClass>): List<List<BlockImpl>> {
    var customMutableList: MutableList<List<BlockImpl>> = mutableListOf()

    functionList.forEach {
        customMutableList = customMutableList.apply {
            add(it.codeBlocksList as List<BlockImpl>)
        }
    }

    val customList: List<List<BlockImpl>> = customMutableList
    return customList
}