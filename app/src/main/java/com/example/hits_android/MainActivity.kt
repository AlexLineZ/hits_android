package com.example.hits_android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.ThemePreference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Hits_androidTheme {
                NavigationSystem(this@MainActivity)
            }
        }
    }
}

@Composable
fun NavigationSystem(context: Context) {
    val navController = rememberNavController()
    val viewModel: ReorderListViewModel = viewModel(
        factory = ReorderListViewModelFactory(
            sharedPreferences = ThemePreference.getInstance(context)
        )
    )

    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController, viewModel) }
        composable("main") { MainScreen(navController, viewModel) }
    }
}