package com.example.hits_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val isDarkTheme = if (isSystemInDarkTheme()) MyAppTheme.Dark else MyAppTheme.Light
            val selectedTheme = remember { mutableStateOf (isDarkTheme) }

            Hits_androidTheme(selectedTheme = selectedTheme.value) {
                Navigator(screen = StartScreen(selectedTheme))
            }
        }
    }
}