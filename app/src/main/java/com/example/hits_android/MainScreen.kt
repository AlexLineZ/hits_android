package com.example.hits_android


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import com.example.hits_android.ui.theme.Hits_androidTheme
import com.example.hits_android.blocks.*
import com.example.hits_android.expressionParser.variables
import com.example.hits_android.model.FlowViewModel
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.model._output
import kotlinx.coroutines.DelicateCoroutinesApi
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


class MainScreen : Screen {
    @Composable
    override fun Content() {
        Hits_androidTheme {
            NavBar()
        }
    }
}

@Composable
fun Sandbox(
    vm: ReorderListViewModel
) {
    Box(
        Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth()
    ) {
        VerticalReorderList(vm = vm)
    }
}


@Composable
private fun VerticalReorderList(
    vm: ReorderListViewModel
) {
    val state = rememberReorderableLazyListState(
        onMove = vm::moveBlock,
        canDragOver = vm::isDogDragOverEnabled
    )
    LazyColumn(
        state = state.listState,
        modifier = Modifier.reorderable(state),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vm.codeBlocksList, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val scale = animateFloatAsState(if (dragging) 1.1f else 1.0f)
                val elevation = if (dragging) 8.dp else 0.dp
                if (item.isDragOverLocked) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .scale(scale.value)
                            .shadow(elevation, RoundedCornerShape(24.dp))
                    ) {
                        item.BlockComposable(item, vm.codeBlocksList)
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .detectReorderAfterLongPress(state)
                            .scale(scale.value)
                            .shadow(elevation, RoundedCornerShape(24.dp))
                            .clickable(
                                onClick = {
                                    Log.d("s", "${vm.codeBlocksList.size}")
                                }
                            )
                    ) {
                        item.BlockComposable(item, vm.codeBlocksList)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    vm: ReorderListViewModel
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                    add(
                                        vm.codeBlocksList.size - 1,
                                        when (item.blockName) {
                                            "assignmentBlock" -> {
                                                AssignmentBlock(key = "${vm.codeBlocksList.size}")
                                            }

//                                            "beginBlock" -> {
//                                                BeginBlock(key = "${vm.codeBlocksList.size}")
//                                            }

                                            "breakBlock" -> {
                                                BreakBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "continueBlock" -> {
                                                ContinueBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "ElseBlock" -> {
                                                ElseBlock(key = "${vm.codeBlocksList.size}")
                                            }

//                                            "endBlock" -> {
//                                                EndBlock(key = "${vm.codeBlocksList.size}")
//                                            }

                                            "IfBlock" -> {
                                                IfBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "initArrayBlock" -> {
                                                InitializeArrayBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "initVarBlock" -> {
                                                InitializeVarBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "outputBlock" -> {
                                                OutputBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            "WhileBlock" -> {
                                                WhileBlock(key = "${vm.codeBlocksList.size}")
                                            }

                                            else -> {
                                                AssignmentBlock(key = "${vm.codeBlocksList.size}")
                                            }
                                        }
                                    )
                                    if (item.blockName == "ElseBlock" ||
                                        item.blockName == "IfBlock" ||
                                        item.blockName == "WhileBlock"
                                    ) {
                                        add(
                                            vm.codeBlocksList.size,
                                            EndBlock(key = "${vm.codeBlocksList.size + 2}")
                                        )
                                        add(
                                            vm.codeBlocksList.size,
                                            BeginBlock(key = "${vm.codeBlocksList.size + 1}")
                                        )
                                    }
                                }
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
    val viewModel: FlowViewModel = viewModel()
    var isOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            NavHost(
                navController = navController,
                startDestination = "Coding"
            ) {
                composable("Coding") {
                    Surface {
                        Column {
                            BottomBar(vm)
                            Sandbox(vm)
                        }
                    }
                }

                composable("Console") {
                    Console(navController, viewModel)
                }

                composable("Settings") {
                    Settings(navController)
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape
            ) {
                val bottomMenuItemsList = prepareBottomMenu()
                var selectedItem by remember { mutableStateOf("Coding") }

                bottomMenuItemsList.forEachIndexed { index, menuItem ->
                    if (index == 2) {
                        BottomNavigationItem(
                            selected = false,
                            onClick = {},
                            enabled = false,
                            label = { }
                        )
                    }

                    if (menuItem.label == "Start") {
                        val isProgramRunning by viewModel.isProgramRunning.collectAsState()
                        BottomNavigationItem(
                            selected = (selectedItem == menuItem.label),
                            onClick = {
                                selectedItem = menuItem.label
                                if (isProgramRunning) {
                                    viewModel.stopProgram()
                                } else {
                                    viewModel.startProgram()
                                    navController.navigate("Console")
                                }
                            },
                            enabled = true,
                            label = {
                                val buttonIcon =
                                    if (isProgramRunning) Icons.Default.Close else Icons.Default.PlayArrow
                                Icon(
                                    painter = rememberVectorPainter(buttonIcon),
                                    contentDescription = menuItem.label
                                )
                            }
                        )
                    } else {
                        BottomNavigationItem(
                            selected = (selectedItem == menuItem.label),
                            onClick = {
                                selectedItem = menuItem.label
                                navController.navigate(menuItem.label)
                            },
                            enabled = true,
                            label = {
                                Icon(
                                    painter = rememberVectorPainter(menuItem.icon),
                                    contentDescription = menuItem.label
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isOpen = !isOpen
                }
            ) {
                val buttonIcon =
                    if (!isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                Icon(
                    painter = rememberVectorPainter(buttonIcon),
                    contentDescription = "forYura"
                )
            }
        }
    )
}}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    bottomMenuItemsList.add(BottomMenuItem(label = "Settings", icon = Icons.Default.Settings))
    bottomMenuItemsList.add(BottomMenuItem(label = "Coding", icon = Icons.Default.Build))
    bottomMenuItemsList.add(BottomMenuItem(label = "Console", icon = Icons.Default.DateRange))
    bottomMenuItemsList.add(BottomMenuItem(label = "Start", icon = Icons.Default.PlayArrow))

    return bottomMenuItemsList
}

data class BottomMenuItem(val label: String, val icon: ImageVector)

@Composable
fun Console(navController: NavController, viewModel: FlowViewModel) {
    val output by viewModel.output.collectAsState()

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
                text = output,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun Settings(navController: NavController) {

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
                    text = "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
        item {
            Text(
                text = "Авторы:\nАлексей Ковкин, Юрий Ситдиков, Александр Кирсанов",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@OptIn(DelicateCoroutinesApi::class)
fun TestProgram() {
    variables.clear()
    blockList.clear()
    blockIndex = 0
    _output.value = ""

    val s0 = InitializeVarBlock(-1, -1, "S", "S", true)
    s0.testBlock("a", "Int", "0;")

    val s1 = WhileBlock(-1, -1, "S", "S", true)
    s1.testBlock("a < 1;")

    val s10 = BeginBlock(-1, -1, "S", "S", true)

    val s2 = AssignmentBlock(-1, -1, "S", "S", true)
    s2.testBlock("a", "a + 1;")

    val s33 = ContinueBlock(-1, -1, "S", "S", true)

    val s4 = AssignmentBlock(-1, -1, "S", "S", true)
    s4.testBlock("a", "a - 1;")

    val s5 = EndBlock(-1, -1, "S", "S", true)

    val s6 = OutputBlock(-1, -1, "S", "S", true)
    s6.testBlock("a;")

}



