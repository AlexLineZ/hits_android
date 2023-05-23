package com.example.hits_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun CodeBlocksScreen(
    vm: ReorderListViewModel
) {
    Box(
        Modifier
            .fillMaxHeight(0.93f)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            VerticalReorderList(vm = vm)
        }
    }
}