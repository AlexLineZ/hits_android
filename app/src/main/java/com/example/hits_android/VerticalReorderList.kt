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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.WhileBlock
import com.example.hits_android.blocks.blockList
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
    val state = rememberReorderableLazyListState(
        onMove = vm::moveBlock,
        canDragOver = vm::isDogDragOverEnabled
    )
    LazyColumn(
        state = state.listState,
        modifier = Modifier.reorderable(state).padding(start = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vm.codeBlocksList, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val scale = animateFloatAsState(if (dragging) 1.1f else 1.0f)
                val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if ((it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart)) {
                            if (item.isDragOverLocked) {
                                vm.codeBlocksList = vm.codeBlocksList.toMutableList().apply {
                                    removeIf { it.key != "0" && it.key != "1" }
                                }
                                return@rememberDismissState false
                            } else if (item.blockName == "endBlock" || item.blockName == "beginBlock") {
                                return@rememberDismissState false
                            } else {
                                if (item.blockName == "ElseBlock" || item.blockName == "IfBlock" || item.blockName == "WhileBlock") {
                                    RemoveDependentBlocks(item, vm)
                                } else {
                                    vm.codeBlocksList = vm.codeBlocksList.toMutableList().apply {
                                        removeIf { it.key == item.key }
                                    }
                                }
                            }
                            blockList = vm.codeBlocksList.toMutableList()
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

                        if (!(item.isDragOverLocked || item.blockName == "endBlock" || item.blockName == "beginBlock")) {
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
                                item.BlockComposable(item, vm.codeBlocksList)
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .detectReorderAfterLongPress(state)
                                    .scale(scale.value)
                                    .shadow(elevation.value, RoundedCornerShape(24.dp))
                                    .clickable(
                                        onClick = {
                                            Log.d("a", "${vm.codeBlocksList}")
                                        }
                                    )
                            ) {
                                item.BlockComposable(item, vm.codeBlocksList)
                            }
                        }
                    }
                )
            }
        }
    }
}


fun RemoveDependentBlocks(item: Block, vm: ReorderListViewModel) {
    when (item.blockName) {
        "ElseBlock" -> {
            item as ElseBlock
            vm.codeBlocksList =
                vm.codeBlocksList.toMutableList().apply {
                    removeIf { it.key == item.key || it.key == item.beginKey || it.key == item.endKey }
                }
        }

        "IfBlock" -> {
            item as IfBlock
            vm.codeBlocksList =
                vm.codeBlocksList.toMutableList().apply {
                    removeIf { it.key == item.key || it.key == item.beginKey || it.key == item.endKey }
                }
        }

        "WhileBlock" -> {
            item as WhileBlock
            vm.codeBlocksList =
                vm.codeBlocksList.toMutableList().apply {
                    removeIf { it.key == item.key || it.key == item.beginKey || it.key == item.endKey }
                }
        }
    }
}