package com.example.hits_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.BuildTheme
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.ui.theme.ThemePreference

@Composable
fun StartScreen(navController: NavController, vm: ReorderListViewModel) {
    val theme by vm.theme.collectAsState()

    Hits_androidTheme(BuildTheme(theme)) {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        navController.navigate("main")
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