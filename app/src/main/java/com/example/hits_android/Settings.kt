package com.example.hits_android

import android.content.Context
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.AppThemeBrightness
import com.example.hits_android.ui.theme.AppThemeColor

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
        SavesBuilderComposable(vm, context)

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


var type = "Hui"
@Composable
fun DropdownMenu(item: InitializeVarBlock) {
    val types = listOf("1", "2", "3", "4", "5")
    val selectedType = remember { mutableStateOf<String?>(null) }
    val expanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .fillMaxHeight()
            .clickable(onClick = { expanded.value = true }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = selectedType.value ?: type,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline
        )
    }
    androidx.compose.material3.DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        types.forEach { type ->
            DropdownMenuItem(
                onClick = {
                    selectedType.value = type
                    item.type = type
                    expanded.value = false
                },
                text = { Text(text = type) }
            )
        }
    }
}


@Composable
fun SavesBuilderComposable(vm: ReorderListViewModel, context: Context) {
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
                    SaveFileStorage(context).saveFunctionListToJson(vm.functionsList,"cringe")
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
                .background(MaterialTheme.colorScheme.primary)
                .clickable {

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Load",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                maxLines = 1
            )
        }
    }
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