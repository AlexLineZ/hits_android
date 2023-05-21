package com.example.hits_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Hits_androidTheme {
                NavigationSystem()
            }
        }
    }
}

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()
    val viewModel: ReorderListViewModel = viewModel()

    NavHost(navController = navController, startDestination = "start",) {
        composable("start") { StartScreen(navController, viewModel) }
        composable("main") { MainScreen(navController, viewModel) }
    }
}