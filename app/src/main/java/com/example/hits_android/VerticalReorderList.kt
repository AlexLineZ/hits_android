package com.example.hits_android

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.hits_android.blocks.*
import com.example.hits_android.model.ReorderListViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalReorderList(
    vm: ReorderListViewModel
) {
    val id by vm.currentScreenId.collectAsState()
    val state = rememberReorderableLazyListState(
        onMove = vm::moveBlock,
        canDragOver = vm::isDogDragOverEnabled
    )
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vm.functionsList[id].codeBlocksList, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val scale = animateFloatAsState(if (dragging) 1.1f else 1.0f)
                val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToEnd
                            || it == DismissValue.DismissedToStart
                        ) {
                            if (item.isDragOverLocked
                                && item.blockName != "functionNameBlock"
                                && item.blockName != "functionsArgumentBlock"
                            ) {
                                vm.functionsList[id].codeBlocksList =
                                    vm.functionsList[id].codeBlocksList.toMutableList().apply {
                                        removeIf {
                                            it.key != "0"
                                                    && it.key != "1"
                                                    && it.blockName != "functionNameBlock"
                                                    && it.blockName != "functionsArgumentBlock"
                                        }
                                    }
                                return@rememberDismissState false
                            } else if (item.blockName == "endBlock"
                                || item.blockName == "beginBlock"
                                || item.blockName == "functionNameBlock"
                                || item.blockName == "functionsArgumentBlock"
                            ) {
                                return@rememberDismissState false
                            } else {
                                if (item.blockName == "elseBlock"
                                    || item.blockName == "ifBlock"
                                    || item.blockName == "whileBlock"
                                ) {
                                    RemoveDependentBlocks(item, vm, id)
                                } else {
                                    vm.functionsList[id].codeBlocksList =
                                        vm.functionsList[id].codeBlocksList.toMutableList().apply {
                                            removeIf { it.key == item.key }
                                        }
                                }
                            }
                        }
                        true
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                        val color by animateColorAsState(
                            targetValue =
                            when (dismissState.targetValue) {
                                DismissValue.Default -> MaterialTheme.colorScheme.errorContainer.copy(
                                    0.1f
                                )

                                DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.errorContainer
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
                            }
                        )
                        val icon = Icons.Default.Delete

                        if (!(item.isDragOverLocked
                                    || item.blockName == "endBlock"
                                    || item.blockName == "beginBlock")
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush =
                                        when (direction) {
                                            DismissDirection.StartToEnd -> Brush.horizontalGradient(
                                                colors = listOf(color.copy(0.6f), color.copy(0f)),
                                                startX = 0f,
                                                endX = 300f
                                            )

                                            DismissDirection.EndToStart -> Brush.horizontalGradient(
                                                colors = listOf(color.copy(0.6f), color.copy(0f)),
                                                startX = 1000f,
                                                endX = 700f
                                            )
                                        }
                                    )
                                    .padding(horizontal = 25.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "deleteIcon",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.align(
                                        when (direction) {
                                            DismissDirection.StartToEnd -> Alignment.CenterStart
                                            DismissDirection.EndToStart -> Alignment.CenterEnd
                                        }
                                    )
                                )
                            }
                        }
                    },
                    directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                    dismissContent = {
                        if (item.isDragOverLocked) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(scale.value)
                                    .shadow(elevation.value, RoundedCornerShape(24.dp))
                            ) {
                                item.BlockComposable(item)
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .detectReorderAfterLongPress(state)
                                    .scale(scale.value)
                                    .shadow(elevation.value, RoundedCornerShape(24.dp))
                                    .padding(
                                        start = calculatePadding(
                                            vm.functionsList[id].codeBlocksList,
                                            item.key
                                        )
                                    )
                                    .clickable(
                                        onClick = {
                                            Log.d("a", "${vm.functionsList[id].codeBlocksList}")
                                        }
                                    )
                            ) {
                                item.BlockComposable(item)
                            }
                        }
                    }
                )
            }
        }
    }
}


fun RemoveDependentBlocks(item: Block, vm: ReorderListViewModel, id: Int) {
    when (item.blockName) {
        "elseBlock" -> {
            item as ElseBlock
            vm.functionsList[id].codeBlocksList =
                vm.functionsList[id].codeBlocksList.toMutableList().apply {
                    removeIf {
                        it.key == item.key
                                || it.key == item.beginKey
                                || it.key == item.endKey
                    }
                }
        }

        "ifBlock" -> {
            item as IfBlock
            vm.functionsList[id].codeBlocksList =
                vm.functionsList[id].codeBlocksList.toMutableList().apply {
                    removeIf {
                        it.key == item.key
                                || it.key == item.beginKey
                                || it.key == item.endKey
                    }
                }
        }

        "whileBlock" -> {
            item as WhileBlock
            vm.functionsList[id].codeBlocksList =
                vm.functionsList[id].codeBlocksList.toMutableList().apply {
                    removeIf {
                        it.key == item.key
                                || it.key == item.beginKey
                                || it.key == item.endKey
                    }
                }
        }
    }
}