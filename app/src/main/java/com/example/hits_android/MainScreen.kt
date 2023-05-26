package com.example.hits_android


import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.buildTheme

@Composable
fun MainScreen(
    navController: NavHostController,
    vm: ReorderListViewModel,
    context: Context
) {
    val theme by vm.theme.collectAsState()
    Hits_androidTheme(buildTheme(theme)) {
        BottomNavigation(vm = vm, context)
    }
}