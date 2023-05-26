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
import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.appmodel.data.localstorage.SaveRoomStorage
import com.example.hits_android.appmodel.data.repository.SaveRepository
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.model.ReorderListViewModelFactory
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.ThemePreference


class MainActivity : ComponentActivity() {
    var isCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Hits_androidTheme {
                NavigationSystem(this@MainActivity)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setContent {}
    }

    override fun onResume() {
        super.onResume()
        setContent {
            Hits_androidTheme {
                NavigationSystem(this@MainActivity)
            }
        }
    }

    @Composable
    fun NavigationSystem(context: Context) {
        val navController = rememberNavController()
        val viewModel: ReorderListViewModel = viewModel(
            factory = ReorderListViewModelFactory(
                sharedPreferences = ThemePreference.getInstance(context),
                repository = SaveRepository(
                    database = SaveRoomStorage.getInstance(context),
                    fileStorage = SaveFileStorage(context),
                    context = context
                )
            )
        )


        if (isCreated) {
            MainScreen(viewModel, context)
        } else {
            NavHost(navController = navController, startDestination = "start") {
                composable("start") { StartScreen(navController, viewModel) }
                composable("main") { MainScreen(viewModel, context) }
            }

            isCreated = true
        }
    }
}