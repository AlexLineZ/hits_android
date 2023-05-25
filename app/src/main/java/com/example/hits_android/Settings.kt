package com.example.hits_android

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.model.SavesViewModel
import com.example.hits_android.ui.theme.AppThemeBrightness
import com.example.hits_android.ui.theme.AppThemeColor
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun Settings(vm: ReorderListViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        ThemeBuilderComposable(vm)
        SavesBuilderComposable(vm)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Авторы:\nАлексей Ковкин\nЮрий Ситдиков\nАлександр Кирсанов",
                modifier = Modifier.padding(15.dp),
            )
        }
    }
}


@Composable
fun DropdownMenu(svm: SavesViewModel, vm: ReorderListViewModel) {
    val saves = svm.state.collectAsState().value
    var loadedSave = svm.loadedSave.collectAsState().value
    if (loadedSave.name != "---") {
        vm.parseToFunctionList(loadedSave.functionsList)

        Log.d("a", "${loadedSave.functionsList}")
        loadedSave.name = "---"
    }


    val expanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .fillMaxHeight()
            .clickable(onClick = {
                svm.startGetAllSaves()
                expanded.value = true
            }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Load",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    androidx.compose.material3.DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        saves.forEach { name ->
            DropdownMenuItem(
                onClick = {
                    svm.startGetSave(name.name)
                    expanded.value = false
                },
                text = { Text(text = name.name) }
            )
        }
    }
}

@Composable
fun SavesBuilderComposable(vm: ReorderListViewModel) {
    val svm: SavesViewModel = koinViewModel()
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
                    val customList = createFunctionListToJSON(vm.functionsList)
                    val saveModel = SaveModel(
                        functionsList = customList,
                        name = UUID
                            .randomUUID()
                            .toString(),
                        date = System.currentTimeMillis()
                    )
                    svm.startWriteSave(saveModel)
                },
            contentAlignment = Alignment.Center
        ) {
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

fun createFunctionListToJSON(functionList: List<FunctionClass>): List<List<Block>> {
    var customMutableList: MutableList<List<Block>> = mutableListOf()

    functionList.forEach {
        customMutableList = customMutableList.apply {
            add(it.codeBlocksList)
        }
    }

    val customList: List<List<Block>> = customMutableList
    return customList
}


@Composable
fun ThemeBuilderComposable(vm: ReorderListViewModel) {
    val theme by vm.theme.collectAsState()
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Theme",
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
                    vm.setCurrentTheme(
                        Pair(
                            when (theme.first) {
                                AppThemeBrightness.Light -> AppThemeBrightness.Dark
                                AppThemeBrightness.Dark -> AppThemeBrightness.System
                                AppThemeBrightness.System -> AppThemeBrightness.Light
                            },
                            theme.second
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (theme.first) {
                    AppThemeBrightness.Light -> "Light"
                    AppThemeBrightness.Dark -> "Dark"
                    AppThemeBrightness.System -> "System"

                },
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
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    vm.setCurrentTheme(
                        Pair(
                            theme.first,
                            when (theme.second) {
                                AppThemeColor.Green -> AppThemeColor.Purple
                                AppThemeColor.Purple -> AppThemeColor.Pink
                                AppThemeColor.Pink -> AppThemeColor.Blue
                                AppThemeColor.Blue -> AppThemeColor.Red
                                AppThemeColor.Red -> AppThemeColor.Yellow
                                AppThemeColor.Yellow -> AppThemeColor.Green
                            }
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (theme.second) {
                    AppThemeColor.Green -> "Green"
                    AppThemeColor.Purple -> "Purple"
                    AppThemeColor.Pink -> "Pink"
                    AppThemeColor.Blue -> "Blue"
                    AppThemeColor.Red -> "Red"
                    AppThemeColor.Yellow -> "Yellow"
                },
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                maxLines = 1
            )
        }
    }
}