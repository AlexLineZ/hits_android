package com.example.hits_android.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.MainBlock
import org.burnoutcrew.reorderable.ItemPosition

class ReorderListViewModel: ViewModel() {
    var dogs by mutableStateOf(List(21) {
        if(it != 0 && it != 9) MainBlock(key = "$it", isDragOverLocked = false) else MainBlock(key = "$it")
    })
    var cats by mutableStateOf(List(20) { MainBlock(key = "$it") })

    fun moveDog(from: ItemPosition, to: ItemPosition) {
        dogs = dogs.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        Log.d("s", "${dogs.size}")
    }

    fun isDogDragOverEnabled(draggedOver: ItemPosition, dragging: ItemPosition) = dogs.getOrNull(draggedOver.index)?.isDragOverLocked != true
}