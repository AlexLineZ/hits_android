package com.example.hits_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.hits_android.ui.theme.Hits_androidTheme

class StartScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Hits_androidTheme {
            Surface {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            navigator.push(MainScreen())
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(text = "Another activity")
                    }
                }
            }
        }
    }
}