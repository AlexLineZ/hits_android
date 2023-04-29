package com.example.hits_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.ui.theme.Hits_androidTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var first = Block("first", Color.Green, 0.0)
        var second = Block("second", Color.Red, 40.0 * 8)
        var third = Block("third", Color.Blue, 80.0 * 8)

        var blockList = mutableListOf<Block>()

        blockList.add(first)
        blockList.add(second)
        blockList.add(third)

        setContent {
            Hits_androidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    Blocks(blockList)
                }
            }
        }
    }
}

@Composable
fun display(blocks: MutableList<Block>) {
    var blocksCopy = blocks.toMutableList()

    for (a in 0 .. blocksCopy.size - 1) {
        for (b in a + 1 .. blocksCopy.size - 1) {
            if (blocksCopy[a].getHeight() > blocksCopy[b].getHeight()) {
                var temp = blocksCopy[a]
                blocksCopy[a] = blocksCopy[b]
                blocksCopy[b] = temp
            }
        }
    }

    Text(
        text = blocksCopy[0].getName()
    )
}
// Группа перетаскиваемых блоков
@Composable
fun Blocks(blocks: MutableList<Block>) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0..blocks.size - 1){
            blocks[i] = DraggableBox(blocks[i])
        }

        display(blocks)
    }
}

// Перетаскиваемый блок
@Composable
fun DraggableBox(block: Block) : Block {
    var offsetX by remember { mutableStateOf(0.0) }
    var offsetY by remember { mutableStateOf(0.0) }

    Box (
        Modifier
            .offset {
                IntOffset(
                    x = offsetX.roundToInt(),
                    y = offsetY.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .background(color = block.getColor())
            .padding(25.dp)
            .size(250.dp, 40.dp)
    ) {
        Text (
            modifier = Modifier.align(Alignment.Center),
            text = block.getName(),
            fontSize = 30.sp,
            color = Color(0xFFFFEEFF),
            textAlign = TextAlign.Right,
            fontWeight = FontWeight.Bold
                )
    }

    block.changePosition(offsetX, offsetY)

    return block
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Hits_androidTheme {
        Greeting("Android")
    }
}