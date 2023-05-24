package com.example.hits_android

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hits_android.model.FlowViewModel
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.AppThemeBrightness

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int = R.drawable.ic_bottom_home,
    val icon_focused: Int = R.drawable.ic_bottom_home_focused
) {

    object BlocksBar : BottomBarScreen(
        route = "blocksbar",
        title = "BlocksBar",
        icon = R.drawable.ic_bottom_blocks_open,
        icon_focused = R.drawable.ic_bottom_blocks_close
    )

    object Coding : BottomBarScreen(
        route = "coding",
        title = "Coding",
        icon = R.drawable.ic_bottom_home,
        icon_focused = R.drawable.ic_bottom_home_focused
    )

    object Console : BottomBarScreen(
        route = "console",
        title = "Console",
        icon = R.drawable.ic_bottom_report,
        icon_focused = R.drawable.ic_bottom_report_focused
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_bottom_profile,
        icon_focused = R.drawable.ic_bottom_profile_focused
    )

    object Start : BottomBarScreen(
        route = "start",
        title = "Start",
        icon = R.drawable.ic_bottom_play,
        icon_focused = R.drawable.ic_bottom_stop
    )
}

@Composable
fun BottomNavigation(
    vm: ReorderListViewModel,
    context: Context
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBottomBar(
                navController = navController,
                vm = vm
            )
        }
    ) {
        Modifier.padding(it)
        BottomNavigationGraph(
            navController = navController,
            vm = vm,
            context
        )
    }
}

@Composable
fun NavigationBottomBar(
    navController: NavHostController,
    vm: ReorderListViewModel
) {
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
                viewModel = viewModel,
                vm = vm
            )
        }
    }
}

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    vm: ReorderListViewModel,
    context: Context
) {

    val viewModel: FlowViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Coding.route
    ) {
        composable(route = BottomBarScreen.Coding.route) {
            Surface {
                Column {
                    CodeBlocksScreen(vm)
                }
            }
        }
        composable(route = BottomBarScreen.Console.route) {
            Console(viewModel)
        }
        composable(route = BottomBarScreen.Settings.route) {
            Settings(vm, context)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    viewModel: FlowViewModel,
    vm: ReorderListViewModel
) {
    val isProgramRunning by viewModel.isProgramRunning.collectAsState()
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val theme by vm.theme.collectAsState()
    val functionList = vm.functionsList

    val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else Color.Transparent

    val contentColor =
        when (theme.first) {
            AppThemeBrightness.Light -> if (selected) Color.White else Color.Black
            AppThemeBrightness.Dark -> Color.White
            AppThemeBrightness.System -> if (isSystemInDarkTheme()) Color.White else (if (selected) Color.White else Color.Black)
        }

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
                        viewModel.startProgram(functionList)

                        if (currentDestination?.route != BottomBarScreen.Console.route) {
                            navController.navigate(BottomBarScreen.Console.route)
                        }
                    }
                } else if (screen == BottomBarScreen.BlocksBar) {
                    blocksBarSelected.value = !blocksBarSelected.value

                    if (currentDestination?.route != BottomBarScreen.Coding.route) {
                        navController.navigate(BottomBarScreen.Coding.route)
                    }
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
            AnimatedVisibility(visible = blocksBarSelected.value && screen == BottomBarScreen.BlocksBar) {
                BlockSelectionList(vm = vm)
            }
            AnimatedVisibility(visible = selected && screen != BottomBarScreen.Start && screen != BottomBarScreen.BlocksBar) {
                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}