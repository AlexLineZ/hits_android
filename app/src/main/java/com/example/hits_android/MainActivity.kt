package com.example.hits_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.hits_android.ui.theme.Hits_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hits_androidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DefaultPreview()
                }
            }
        }
    }
}

@Composable
fun BoxScope.Greeting(name: String) {
    Text(text = "Hello $name!", textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxSize()
        .align(Alignment.Center))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Hits_androidTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Greeting("YuraLox")
        }
    }
}