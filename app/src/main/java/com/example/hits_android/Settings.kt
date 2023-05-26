package com.example.hits_android

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.model.ReorderListViewModel

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
        InformationSheetComposable()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Created by:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Alexey Kovkin\n" +
                            "Yuriy Sitdikov\n" +
                            "Alexander Kirsanov"
                )
            }
        }
    }
}
