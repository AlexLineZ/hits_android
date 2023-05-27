package com.example.hits_android

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun CodeBlocksScreen(
    vm: ReorderListViewModel
) {
    Box(
        Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FunctionSelection(vm = vm)
            VerticalReorderList(vm = vm)
        }
    }
}