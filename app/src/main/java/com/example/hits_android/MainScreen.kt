package com.example.hits_android


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hits_android.blocks.*
import com.example.hits_android.expressionParser.Type
import com.example.hits_android.expressionParser.variables


class MainScreen:Screen {
    @Composable
    override fun Content() {
        val vm = ReorderListViewModel()
        Hits_androidTheme {
            NavBar()
        }
    }
}

// нужно будет это убрать потом
@Composable
fun DropDownMenu() {
    var isExpanded by remember { mutableStateOf(false)}
    val list = listOf("Int", "Bool", "String")
    var selectedItem by remember { mutableStateOf("Int")}
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
    val state = rememberReorderableLazyListState(onMove = vm::moveBlock, canDragOver = vm::isDogDragOverEnabled)
    LazyColumn(
        state = state.listState,
        modifier = Modifier.reorderable(state),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(vm.codeBlocksList, { item -> item.key }) { item ->
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
                            .clickable(
                                onClick = {
                                    Log.d("s", "${vm.codeBlocksList.size}")
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
        items(vm.blockSelectionList, { item -> item.key }) { item ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp, 75.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Red)
                    .clickable(
                        onClick = {
                            vm.codeBlocksList = vm.codeBlocksList
                                .toMutableList()
                                .apply {
                                    add(vm.codeBlocksList.size - 1,
                                        when (item.key) {
                                            "0" -> {
                                                BeginBlock(key = "${vm.codeBlocksList.size}")
                                            }
                                            "1" -> {
                                                EndBlock(key = "$${vm.codeBlocksList.size}")
                                            }
                                            else -> {
                                                InitializeVarBlock(key = "$${vm.codeBlocksList.size}")
                                            }
                                        })
                                }
                            Log.d("s", "${vm.codeBlocksList.size}")
                        }
                    )
            ) {
                Text(item.title)
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NavBar() {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val vm = ReorderListViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { NavBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                TestProgram()
                navController.navigate("Console")
            }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start")
            }
        },
        scaffoldState = scaffoldState,
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) {
        NavHost(
            navController = navController,
            startDestination = "Coding"
        ) {
            composable("Coding") {
                Surface() {
                    Column {
                        BottomBar(vm)
                        Sandbox(vm)
                    }
                }
            }
            composable("Console") {
                Console(navController)
            }
        }
    }
}

@Composable
fun NavBottomBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()

    var selectedItem by remember {
        mutableStateOf("Coding")
    }

    BottomAppBar(
        cutoutShape = CircleShape
    ) {
        bottomMenuItemsList.forEachIndexed { index, menuItem ->
            if (index == 1) {
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {},
                    enabled = false
                )
            }

            BottomNavigationItem(
                selected = (selectedItem == menuItem.label),
                onClick = {
                    selectedItem = menuItem.label
                    navController.navigate(menuItem.label)
                },
                icon = {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = menuItem.label
                    )
                },
                enabled = true
            )
        }
    }
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    bottomMenuItemsList.add(BottomMenuItem(label = "Coding", icon = Icons.Default.Build))
    bottomMenuItemsList.add(BottomMenuItem(label = "Console", icon = Icons.Default.DateRange))

    return bottomMenuItemsList
}

data class BottomMenuItem(val label: String, val icon: ImageVector)

@Composable
fun Console(navController: NavController) {
    val viewModel = viewModel<FlowViewModel>()
    val currentValue = viewModel.counterFlow.collectAsState(initial = "1")

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Console",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
        item {
            Text(
                text = currentValue.value,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

fun TestProgram(){
    variables.clear()
    blockList.clear()
    consoleString = ""

    // Размер массива
    var s = InitializeVarBlock(-1, -1, "init", "init", true)
    s.testBlock("size", "10;")

    // Создание массива
    var s0 = InitializeArrayBlock(-1, -1, "init", "init", true)
    s0.testBlock("arr", "size;")

    // Счётчик цикла
    var s1 = InitializeVarBlock(-1, -1, "init", "init", true)
    s1.testBlock("a", "0;")

    // Заполнение массива
    var s2 = WhileBlock(-1, -1, "while", "while", true)
    s2.testBlock("a < size;")
    var s3 = BeginBlock(-1, -1, "begin", "begin", true)

    var s4 = AssignmentBlock(-1, -1, "assign", "assign", true)
    s4.testBlock("arr[a]", "100 - a;")
    var s5 = AssignmentBlock(-1, -1, "assign", "assign", true)
    s5.testBlock("a", "a + 1;")

    var s6 = EndBlock(-1, -1, "end", "end", true)

    // Внешний цикл
    var s7 = InitializeVarBlock(-1, -1, "init", "init", true)
    s7.testBlock("first", "0;")

    var s8 = WhileBlock(-1, -1, "init", "init", true)
    s8.testBlock("first < size;")

    var s9 = BeginBlock(-1, -1, "init", "init", true)

    // Внутренний цикл
    var s10 = InitializeVarBlock(-1, -1, "init", "init", true)
    s10.testBlock("second", "first + 1;")

    var s11 = WhileBlock(-1, -1, "init", "init", true)
    s11.testBlock("second < size;")

    var s12 = BeginBlock(-1, -1, "init", "init", true)

    // Проверка
    var s13 = IfBlock(-1, -1, "init", "init", true)
    s13.testBlock("arr[first] > arr[second];")

    var s14 = BeginBlock(-1, -1, "init", "init", true)

    // Свап
    var s15 = InitializeVarBlock(-1, -1, "init", "init", true)
    s15.testBlock("temp", "arr[first];")
    var s16 = AssignmentBlock(-1, -1, "init", "init", true)
    s16.testBlock("arr[first]", "arr[second];")
    var s17 = AssignmentBlock(-1, -1, "init", "init", true)
    s17.testBlock("arr[second]", "temp;")

    var s18 = EndBlock(-1, -1, "init", "init", true)

    var s19 = AssignmentBlock(-1, -1, "init", "init", true)
    s19.testBlock("second", "second + 1;")

    var s20 = EndBlock(-1, -1, "init", "init", true)

    var s21 = AssignmentBlock(-1, -1, "init", "init", true)
    s21.testBlock("first", "first + 1;")

    var s22 = EndBlock(-1, -1, "init", "init", true)

    // Вывод результата
    var s23 = InitializeVarBlock(-1, -1, "init", "init", true)
    s23.testBlock("index", "0;")

    var s24 = WhileBlock(-1, -1, "init", "init", true)
    s24.testBlock("index < size;")

    var s25 = BeginBlock(-1, -1, "init", "init", true)

    var s26 = OutputBlock(-1, -1, "init", "init", true)
    s26.testBlock("arr[index];")

    var s27 = AssignmentBlock(-1, -1, "init", "init", true)
    s27.testBlock("index", "index + 1;")

    var s28 = EndBlock(-1, -1, "init", "init", true)


    while (blockIndex < blockList.size) {
        blockList[blockIndex].runCodeBlock()
    }
}



