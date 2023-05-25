package com.example.hits_android.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.appmodel.data.repository.SaveRepository
import com.example.hits_android.blocks.AssignmentBlock
import com.example.hits_android.blocks.BeginBlock
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.BreakBlock
import com.example.hits_android.blocks.CallFunctionBlock
import com.example.hits_android.blocks.ContinueBlock
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.EndBlock
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.blocks.FunctionNameBlock
import com.example.hits_android.blocks.FunctionsArgumentBlock
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.InitializeArrayBlock
import com.example.hits_android.blocks.InitializeVarBlock
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

class ReorderListViewModel(
    private val sharedPreferences: ThemePreference,
    private val repository: SaveRepository
) : ViewModel() {

    private val savedTheme = sharedPreferences.getSavedTheme()
    private var _theme = MutableStateFlow(savedTheme)

    val theme: StateFlow<Pair<AppThemeBrightness, AppThemeColor>> = _theme.asStateFlow()

    private var _currentScreenId = MutableStateFlow(0)
    val currentScreenId: StateFlow<Int> = _currentScreenId.asStateFlow()


    private var main: FunctionClass by mutableStateOf(
        FunctionClass(
            functionName = "main",
            id = 0,
            mainBlockTitle = "Start program",
            finishBlockTitle = "End program"
        )
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
            ElseBlock(key = "5"),
            WhileBlock(key = "6"),
            CallFunctionBlock(key = "7"),
            BreakBlock(key = "8"),
            ContinueBlock(key = "9")
        )
    )

    var keyCount = 10

    init {
        blockList.clear()
    }

    fun moveBlock(from: ItemPosition, to: ItemPosition) {
        functionsList[currentScreenId.value].codeBlocksList =
            functionsList[currentScreenId.value].codeBlocksList.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
    }


    fun isDogDragOverEnabled(draggedOver: ItemPosition, dragging: ItemPosition): Boolean {
        return functionsList[currentScreenId.value].codeBlocksList.getOrNull(draggedOver.index)?.isDragOverLocked != true
    }


    fun addBlock(item: Block) {
        functionsList[currentScreenId.value].codeBlocksList =
            functionsList[currentScreenId.value].codeBlocksList
                .toMutableList()
                .apply {
                    add(
                        functionsList[currentScreenId.value].codeBlocksList.size - 1,
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
                            functionsList[currentScreenId.value].codeBlocksList.size,
                            EndBlock(key = "${keyCount + 2}")
                        )
                        add(
                            functionsList[currentScreenId.value].codeBlocksList.size,
                            BeginBlock(key = "${keyCount + 1}")
                        )
                        keyCount += 2
                    }
                }
    }


    fun addFunction() {
        val newFunction: FunctionClass by mutableStateOf(
            FunctionClass(id = functionsList.size)
        )

        functionsList = functionsList
            .toMutableList()
            .apply {
                add(functionsList.size, newFunction)
            }

        functionsList[functionsList.size - 1].codeBlocksList =
            functionsList[functionsList.size - 1].codeBlocksList
                .toMutableList()
                .apply {
                    add(0, FunctionNameBlock(key = "${++keyCount}", isDragOverLocked = true))
                    add(1, FunctionsArgumentBlock(key = "${++keyCount}", isDragOverLocked = true))
                }
    }

    fun setCurrentTheme(newTheme: Pair<AppThemeBrightness, AppThemeColor>) {
        sharedPreferences.apply {
            saveThemeBrightness(newTheme.first)
            saveThemeColor(newTheme.second)
        }

        _theme.value = newTheme
    }

    fun setCurrentScreenId(newId: Int) {
        _currentScreenId.value = newId
    }
}


