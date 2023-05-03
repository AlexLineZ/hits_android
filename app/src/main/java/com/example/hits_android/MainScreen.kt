package com.example.hits_android

import android.media.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.compose.Hits_androidTheme
import kotlin.math.roundToInt

class MainScreen:Screen {

    @Composable
    override fun Content() {
        Hits_androidTheme {
            Surface() {
                Sandbox()
                BottomBar()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ZoomableComposablePreview() {
    Sandbox()
    BottomBar()
}

@Composable
fun Sandbox() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    val maxScale = 40f

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    scale = scale.coerceIn(0.1f, maxScale)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
    ) {
        DragableBlock()
        DragableBlock()
        DragableBlock()
        DragableBlock()
        DragableBlock()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragableBlock() {
    val haptic = LocalHapticFeedback.current
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember{ mutableStateOf(0f) }

    Box(modifier = Modifier
        .offset {
            IntOffset(x = offsetX.roundToInt(), y = offsetY.roundToInt())
        }
        .size(100.dp)
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            ) { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
            }
        }
    ){
        Image(
            painter = painterResource(R.drawable.maxresdefault),
            contentDescription = "Papanya",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BottomBar() {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.align(Alignment.BottomStart)
        ) {
            LazyRow () {
                items(count = 20) {
                    Image(
                        painter = painterResource(R.drawable.maxresdefault),
                        contentDescription = "Papanya",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(30.dp)
                    )
                }
            }
        }
    }
}