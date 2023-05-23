package com.example.hits_android.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.AssignmentBlock
import com.example.hits_android.blocks.BeginBlock
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.BreakBlock
import com.example.hits_android.blocks.CallFunctionBlock
import com.example.hits_android.blocks.ContinueBlock
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.EndBlock
import com.example.hits_android.blocks.FinishProgramBlock
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.InitializeArrayBlock
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.blocks.MainBlock
import com.example.hits_android.blocks.OutputBlock
import com.example.hits_android.blocks.WhileBlock
import com.example.hits_android.blocks.blockList
import com.example.hits_android.ui.theme.AppThemeBrightness
import com.example.hits_android.ui.theme.AppThemeColor
import com.example.hits_android.ui.theme.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.burnoutcrew.reorderable.ItemPosition

class ReorderListViewModel(private val sharedPreferences: ThemePreference) : ViewModel() {

    private val savedTheme = sharedPreferences.getSavedTheme()
    private var _theme = MutableStateFlow(savedTheme)

    val theme: StateFlow<Pair<AppThemeBrightness, AppThemeColor>> = _theme.asStateFlow()

//    var codeBlocksList by mutableStateOf(
//        listOf(
//            MainBlock(key = "0", isDragOverLocked = true, title = "Start program"),
//            FinishProgramBlock(key = "1", isDragOverLocked = true, title = "End program")
//        )
//    )

    var main: FunctionClass by mutableStateOf(
        FunctionClass(functionName = "sas1", id = 0)
    )

    var functionsList by mutableStateOf(
        listOf(
            main
        )
    )

    var blockSelectionList by mutableStateOf(
        listOf(
            InitializeVarBlock(key = "0"),
            InitializeArrayBlock(key = "1"),
            OutputBlock(key = "2"),
            AssignmentBlock(key = "3"),
            IfBlock(key = "4"),
            WhileBlock(key = "5"),
            ElseBlock(key = "6"),
            BreakBlock(key = "7"),
            ContinueBlock(key = "8"),
            CallFunctionBlock(key = "9")
        )
    )

    var keyCount = 1

    init {
        blockList.clear()
    }

    fun moveBlock(from: ItemPosition, to: ItemPosition) {
        functionsList.forEach {
            when (it.isOnScreen) {
                true -> {
                    Log.d("a", "${it.codeBlocksList}")
                    it.codeBlocksList = it.codeBlocksList.toMutableList().apply {
                        add(to.index, removeAt(from.index))
                    }

                    blockList =
                        it.codeBlocksList.slice(1..(it.codeBlocksList.size - 2)).toMutableList()
                }

                false -> {}
            }
        }
    }

    fun isDogDragOverEnabled(draggedOver: ItemPosition, dragging: ItemPosition): Boolean {
        functionsList.forEach {
            when (it.isOnScreen) {
                true -> {
                    return it.codeBlocksList.getOrNull(draggedOver.index)?.isDragOverLocked != true
                }

                false -> {}
            }
        }
        return functionsList[0].codeBlocksList.getOrNull(draggedOver.index)?.isDragOverLocked != true
    }


    fun addBlock(item: Block) {
        functionsList.forEach {
            when (it.isOnScreen) {
                true -> {
                    it.codeBlocksList = it.codeBlocksList
                        .toMutableList()
                        .apply {
                            add(
                                it.codeBlocksList.size - 1,
                                when (item.blockName) {
                                    "assignmentBlock" -> {
                                        AssignmentBlock(key = "${++keyCount}")
                                    }

                                    "breakBlock" -> {
                                        BreakBlock(key = "${++keyCount}")
                                    }

                                    "continueBlock" -> {
                                        ContinueBlock(key = "${++keyCount}")
                                    }

                                    "ElseBlock" -> {
                                        ElseBlock(
                                            key = "${++keyCount}",
                                            beginKey = "${keyCount + 1}",
                                            endKey = "${keyCount + 2}"
                                        )
                                    }

                                    "IfBlock" -> {
                                        IfBlock(
                                            key = "${++keyCount}",
                                            beginKey = "${keyCount + 1}",
                                            endKey = "${keyCount + 2}"
                                        )
                                    }

                                    "initArrayBlock" -> {
                                        InitializeArrayBlock(key = "${++keyCount}")
                                    }

                                    "initVarBlock" -> {
                                        InitializeVarBlock(key = "${++keyCount}")
                                    }

                                    "outputBlock" -> {
                                        OutputBlock(key = "${++keyCount}")
                                    }

                                    "WhileBlock" -> {
                                        WhileBlock(
                                            key = "${++keyCount}",
                                            beginKey = "${keyCount + 1}",
                                            endKey = "${keyCount + 2}"
                                        )
                                    }

                                    "callFunctionBlock" -> {
                                        CallFunctionBlock(key = "${++keyCount}")
                                    }

                                    else -> {
                                        AssignmentBlock(key = "${++keyCount}")
                                    }
                                }
                            )
                            if (item.blockName == "ElseBlock" ||
                                item.blockName == "IfBlock" ||
                                item.blockName == "WhileBlock"
                            ) {
                                add(
                                    it.codeBlocksList.size,
                                    EndBlock(key = "${keyCount + 2}")
                                )
                                add(
                                    it.codeBlocksList.size,
                                    BeginBlock(key = "${keyCount + 1}")
                                )
                                keyCount += 2
                            }
                        }
                }

                false -> {}
            }
        }
    }

    fun addFunction() {
        val newFunctionList: FunctionClass by mutableStateOf(
            FunctionClass(functionName = "sas1", id = functionsList.size)
        )

        functionsList = functionsList
            .toMutableList()
            .apply {
                add(functionsList.size - 1, newFunctionList)
            }
    }

    fun setCurrentTheme(newTheme: Pair<AppThemeBrightness, AppThemeColor>) {
        sharedPreferences.apply {
            saveThemeBrightness(newTheme.first)
            saveThemeColor(newTheme.second)
        }
        _theme.value = newTheme
    }

    fun getCurrentList(): Int {
        functionsList.forEach {
            when (it.isOnScreen) {
                true -> {
                    return it.id
                }

                false -> {}
            }
        }
        return 0
    }
}


