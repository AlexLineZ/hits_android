package com.example.hits_android


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.buildTheme

@Composable
fun MainScreen(
    vm: ReorderListViewModel,
    context: Context
) {
    val theme by vm.theme.collectAsState()
    Hits_androidTheme(buildTheme(theme)) {
        BottomNavigation(vm = vm, context)
    }
}