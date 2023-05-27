package com.example.hits_android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.model.SavesViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.*

@Composable
fun SavesBuilderComposable(vm: ReorderListViewModel) {
    val svm: SavesViewModel = koinViewModel()
    var showDialog by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        Text(
            text = "Saves:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.3f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary)
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
                                Text(text = "Enter save name")
                                TextField(
                                    value = enteredText,
                                    onValueChange = { enteredText = it },
                                    modifier = Modifier.padding(top = 8.dp),
                                    singleLine = true,
                                    placeholder = { Text(text = "name") }
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                            .padding(top = 15.dp),
                                        onClick = {
                                            showDialog = false
                                            val customList =
                                                createFunctionListToJSON(vm.functionsList)
                                            val saveModel = SaveModel(
                                                functionsList = customList,
                                                name = UUID
                                                    .randomUUID()
                                                    .toString(),
                                                date = System.currentTimeMillis(),
                                                realName = enteredText
                                            )
                                            enteredText = ""
                                            svm.startWriteSave(saveModel)
                                        }
                                    ) {
                                        Text(text = "OK")
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "Save",
                    color = MaterialTheme.colorScheme.onPrimary,
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
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                DropdownMenu(svm, vm)
            }
        }
    }
}

@Composable
fun DropdownMenu(svm: SavesViewModel, vm: ReorderListViewModel) {
    val selectedLoad = remember { mutableStateOf<String?>(null) }
    var loadText = "Load"
    val saves = svm.state.collectAsState().value
    val loadedSave = svm.loadedSave.collectAsState().value
    if (loadedSave.name != "---") {
        vm.parseToFunctionList(loadedSave.functionsList)
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
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "deleteSave",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                svm.startDeleteSave(name.name)
                                expanded.value = false
                            }
                    )
                }
            )
        }
    }
}