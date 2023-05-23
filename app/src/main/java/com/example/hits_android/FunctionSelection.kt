package com.example.hits_android

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun FunctionSelection(vm: ReorderListViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.07f)
            .fillMaxWidth()
    ) {
//        items(vm.codeBlocksList, { item -> item.key }) { item ->

//        }
    }
}