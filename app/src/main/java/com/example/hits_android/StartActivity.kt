package com.example.hits_android

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.hits_android.ui.theme.Hits_androidTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Hits_androidTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(onClick = {
                        val intent = Intent(this@StartActivity, TestActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier.background(Color.Gray)
                        .align(Alignment.BottomCenter)
                        .padding(5.dp)
                        .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Another activity")
                    }
                }
            }
        }
    }
}