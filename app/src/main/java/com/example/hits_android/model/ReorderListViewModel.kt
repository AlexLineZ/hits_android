package com.example.hits_android.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.AssignmentBlock
import com.example.hits_android.blocks.BeginBlock
import com.example.hits_android.blocks.ContinueBlock
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.EndBlock
import com.example.hits_android.blocks.FinishProgramBlock
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.InitializeArrayBlock
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.blocks.MainBlock
import com.example.hits_android.blocks.OutputBlock
import com.example.hits_android.blocks.WhileBlock
import org.burnoutcrew.reorderable.ItemPosition

class ReorderListViewModel: ViewModel() {
    var codeBlocksList by mutableStateOf(
        listOf(
            MainBlock(key = "0", isDragOverLocked = true, title = "Start program"),
            FinishProgramBlock(key = "1", isDragOverLocked = true, title = "End program")
        )
    )
    var blockSelectionList by mutableStateOf(
        listOf(
            InitializeVarBlock(key = "0"),
            WhileBlock(key = "1"),
            IfBlock(key = "2"),
            ElseBlock(key = "3"),
            BeginBlock(key = "4"),
            EndBlock(key = "5"),
            ContinueBlock(key = "6"),
            OutputBlock(key = "7"),
            InitializeArrayBlock(key = "8"),
            AssignmentBlock(key = "9")
        )
    )

    fun moveBlock(from: ItemPosition, to: ItemPosition) {
        codeBlocksList = codeBlocksList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        Log.d("s", "${codeBlocksList.size}")
    }

    fun isDogDragOverEnabled(draggedOver: ItemPosition, dragging: ItemPosition) = codeBlocksList.getOrNull(draggedOver.index)?.isDragOverLocked != true
}