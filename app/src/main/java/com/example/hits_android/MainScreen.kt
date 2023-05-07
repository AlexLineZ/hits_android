package com.example.hits_android


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
//import com.example.hits_android.blocks.InitializeBlock
import com.example.hits_android.model.ReorderListViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import kotlin.math.roundToInt
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.window.Dialog
import com.example.hits_android.blocks.*
import com.example.hits_android.expressionParser.variables

class MainScreen:Screen {
    @Composable
    override fun Content() {
        var vm = ReorderListViewModel()
        Hits_androidTheme {
            Surface() {
                Column {
                    Sandbox(vm)
                    BottomBar(vm)
                }
            }
            MyScreen()
        }
    }
}

@Composable
fun Sandbox(
    vm: ReorderListViewModel
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    val maxScale = 40f

    Box(
        Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth()
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
        val haptic = LocalHapticFeedback.current
        var ofsetX by remember { mutableStateOf(0f) }
        var ofsetY by remember { mutableStateOf(0f) }

        Box(modifier = Modifier
            .offset {
                IntOffset(x = ofsetX.roundToInt(), y = ofsetY.roundToInt())
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
                ) { change, dragAmount ->
                    change.consume()
                    ofsetX += dragAmount.x
                    ofsetY += dragAmount.y
                }
            }
        ) {
            VerticalReorderList(vm = vm)
        }
    }
}

@Composable
private fun VerticalReorderList(
    vm: ReorderListViewModel
) {
    val state = rememberReorderableLazyListState(onMove = vm::moveDog, canDragOver = vm::isDogDragOverEnabled)
    LazyColumn(
        state = state.listState,
        modifier = Modifier.reorderable(state),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(vm.dogs, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val scale = animateFloatAsState(if (dragging) 1.1f else 1.0f)
                val alpha = animateFloatAsState(if (dragging) 0.9f else 1.0f)
                val elevation = if (dragging) 8.dp else 0.dp
                if (item.isDragOverLocked) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(scale.value)
                            .shadow(elevation, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Red.copy(alpha = alpha.value))
                    ) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .detectReorderAfterLongPress(state)
                            .scale(scale.value)
                            .shadow(elevation, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.LightGray.copy(alpha = alpha.value))
                            .clickable (
                                onClick = {
                                    Log.d("s", "${vm.dogs.size}")
                                }
                            )
                    ) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainBlockComposable(block: Block) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .background(Color.LightGray)
    ) {
        Text(text = block.blockName, modifier = Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun BottomBar(
    vm: ReorderListViewModel
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(vm.cats, { item -> item.key }) { item ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Red)
                    .clickable (
                        onClick = {
                            vm.dogs = vm.dogs.toMutableList().apply {
                                add(vm.dogs.size - 1, MainBlock(key = "${vm.dogs.size}"))
                            }
                            Log.d("s", "${vm.dogs.size}")
                        }
                    )
            ) {
                Text(item.title)
            }
        }
    }
}

@Composable
fun MyAlertDialog() {
    val onDismissRequest = {}

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(16),
        modifier = Modifier.heightIn(min = 200.dp),
        title = { Text("Console") },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Закрыть"
                    )
                }
            }
        }
    )
}

@Composable
fun MyScreen() {
    var dialogShown by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize().padding(20.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = { dialogShown = true }
        ) {
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }
    }

    if (dialogShown) {
        MyAlertDialog()
    }
}