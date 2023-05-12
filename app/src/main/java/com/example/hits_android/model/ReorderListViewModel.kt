package com.example.hits_android.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.BeginBlock
import com.example.hits_android.blocks.EndBlock
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.blocks.MainBlock
import org.burnoutcrew.reorderable.ItemPosition

class ReorderListViewModel: ViewModel() {
    var codeBlocksList by mutableStateOf(List(10) {
        when (it) {
            0 -> {
                BeginBlock(key = "$it")
            }
            9 -> {
                EndBlock(key = "$it")
            }
            else -> {
                InitializeVarBlock(key = "$it")
            }
        }
    })
    var blockSelectionList by mutableStateOf(List(20) { MainBlock(key = "$it") })

    fun moveBlock(from: ItemPosition, to: ItemPosition) {
        codeBlocksList = codeBlocksList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        Log.d("s", "${codeBlocksList.size}")
    }

    fun isDogDragOverEnabled(draggedOver: ItemPosition, dragging: ItemPosition) = codeBlocksList.getOrNull(draggedOver.index)?.isDragOverLocked != true
}