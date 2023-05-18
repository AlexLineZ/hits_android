package com.example.hits_android


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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.compose.currentBackStackEntryAsState

class MainScreen : Screen {
    @Composable
    override fun Content() {
        Hits_androidTheme {
            BottomNav()
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

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int = R.drawable.ic_bottom_home,
    val icon_focused: Int = R.drawable.ic_bottom_home_focused
) {

    object BlocksBar: BottomBarScreen(
        route = "blocksbar",
        title = "BlocksBar",
        icon = R.drawable.ic_bottom_arrow_up,
        icon_focused = R.drawable.ic_bottom_arrow_down
    )

    object Coding: BottomBarScreen(
        route = "coding",
        title = "Coding",
        icon = R.drawable.ic_bottom_home,
        icon_focused = R.drawable.ic_bottom_home_focused
    )

    object Console: BottomBarScreen(
        route = "console",
        title = "Console",
        icon = R.drawable.ic_bottom_report,
        icon_focused = R.drawable.ic_bottom_report_focused
    )

    object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_bottom_profile,
        icon_focused = R.drawable.ic_bottom_profile_focused
    )

    object Start: BottomBarScreen(
        route = "start",
        title = "Start",
        icon = R.drawable.ic_bottom_play,
        icon_focused = R.drawable.ic_bottom_stop
    )
}

@Composable
fun BottomNav() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavBottomBar(navController = navController) }
    ) {
        Modifier.padding(it)
        BottomNavGraph(
            navController = navController
        )
    }
}

@Composable
fun NavBottomBar(navController: NavHostController) {
    val viewModel: FlowViewModel = viewModel()

    val screens = listOf(
        BottomBarScreen.BlocksBar,
        BottomBarScreen.Coding,
        BottomBarScreen.Console,
        BottomBarScreen.Settings,
        BottomBarScreen.Start
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun BottomNavGraph(navController: NavHostController) {

    val vm = ReorderListViewModel()
    val viewModel: FlowViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Coding.route
    ) {
        composable(route = BottomBarScreen.Coding.route) {
            Surface {
                Column {
                    BottomBar(vm)
                    Sandbox(vm)
                }
            }
        }
        composable(route = BottomBarScreen.Console.route) {
            Console(navController, viewModel)
        }
        composable(route = BottomBarScreen.Settings.route) {
            Settings(navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    viewModel: FlowViewModel
) {
    val isProgramRunning by viewModel.isProgramRunning.collectAsState()
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent

    val contentColor =
        if (selected) Color.White else Color.Black

    val buttonIcon = if (screen == BottomBarScreen.Start) {
        if (isProgramRunning) R.drawable.ic_bottom_stop else R.drawable.ic_bottom_play
    } else {
        if (selected) screen.icon_focused else screen.icon
    }

    val blocksBarSelected = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                if (screen == BottomBarScreen.Start) {
                    if (isProgramRunning) {
                        viewModel.stopProgram()
                    } else {
                        viewModel.startProgram()
                        navController.navigate(BottomBarScreen.Console.route)
                    }
                } else if (screen == BottomBarScreen.BlocksBar) {
                    blocksBarSelected.value = !blocksBarSelected.value
                } else {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (screen == BottomBarScreen.BlocksBar && blocksBarSelected.value) screen.icon_focused else buttonIcon),
                contentDescription = "icon",
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            AnimatedVisibility(visible = selected && screen != BottomBarScreen.Start && screen != BottomBarScreen.BlocksBar) {
                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}



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

    val s0 = FunctionBlock(-1, -1, "S", "S", true)
    s0.testBlock("func", "a: Int")
    val s1 = BeginBlock(-1, -1, "S", "S", true)
    val s2 = OutputBlock(-1, -1, "S", "S", true)
    s2.testBlock("234;")
    val s3 = EndBlock(-1, -1, "S", "S", true)
    val s4 = OutputBlock(-1, -1, "S", "S", true)
    s4.testBlock("432;")
    val s5 = CallFunctionBlock(-1, -1, "S", "S", true)
    s5.testBlock("func", "1;")

}


