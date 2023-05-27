package com.example.hits_android.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hits_android.blocks.AssignmentBlock
import com.example.hits_android.blocks.BeginBlock
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.BlockImpl
import com.example.hits_android.blocks.BreakBlock
import com.example.hits_android.blocks.CallFunctionBlock
import com.example.hits_android.blocks.ContinueBlock
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.EndBlock
import com.example.hits_android.blocks.FinishProgramBlock
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.blocks.FunctionNameBlock
import com.example.hits_android.blocks.FunctionsArgumentBlock
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.InitializeArrayBlock
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.blocks.InputBlock
import com.example.hits_android.blocks.MainBlock
import com.example.hits_android.blocks.OutputBlock
import com.example.hits_android.blocks.ReturnBlock
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
    private val sharedPreferences: ThemePreference
) : ViewModel() {

    private val savedTheme = sharedPreferences.getSavedTheme()
    private var _theme = MutableStateFlow(savedTheme)

    val theme: StateFlow<Pair<AppThemeBrightness, AppThemeColor>> = _theme.asStateFlow()

    private var _currentScreenId = MutableStateFlow(0)
    val currentScreenId: StateFlow<Int> = _currentScreenId.asStateFlow()


    private var main: FunctionClass by mutableStateOf(
        FunctionClass(
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
            InputBlock(key = "2"),
            OutputBlock(key = "3"),
            AssignmentBlock(key = "4"),
            IfBlock(key = "5"),
            ElseBlock(key = "6"),
            WhileBlock(key = "7"),
            CallFunctionBlock(key = "8"),
            ReturnBlock(key = "9"),
            BreakBlock(key = "10"),
            ContinueBlock(key = "11")
        )
    )

    private var keyCount = 100

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

                            "elseBlock" -> {
                                ElseBlock(
                                    key = "${++keyCount}",
                                    beginKey = "${keyCount + 1}",
                                    endKey = "${keyCount + 2}"
                                )
                            }

                            "ifBlock" -> {
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

                            "inputBlock" -> {
                                InputBlock(key = "${++keyCount}")
                            }

                            "outputBlock" -> {
                                OutputBlock(key = "${++keyCount}")
                            }

                            "whileBlock" -> {
                                WhileBlock(
                                    key = "${++keyCount}",
                                    beginKey = "${keyCount + 1}",
                                    endKey = "${keyCount + 2}"
                                )
                            }

                            "callFunctionBlock" -> {
                                CallFunctionBlock(key = "${++keyCount}")
                            }

                            "returnBlock" -> {
                                ReturnBlock(key = "${++keyCount}")
                            }

                            else -> {
                                AssignmentBlock(key = "${++keyCount}")
                            }
                        }
                    )
                    if (item.blockName == "elseBlock" ||
                        item.blockName == "ifBlock" ||
                        item.blockName == "whileBlock"
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

    fun parseToFunctionList(savedList: List<List<BlockImpl>>) {

        var newFunctionsList by mutableStateOf(listOf<FunctionClass>())

        var id = 0
        savedList.forEach { list ->
            val newFunction: FunctionClass by mutableStateOf(
                FunctionClass(id = id)
            )
            newFunction.codeBlocksList = newFunction.codeBlocksList.toMutableList()
                .apply {
                    removeIf { true }
                }

            list.forEach {
                newFunction.codeBlocksList = newFunction.codeBlocksList.toMutableList().apply {
                    add(parseBlock(it))
                }
            }

            newFunctionsList = newFunctionsList.toMutableList().apply {
                add(newFunction)
            }

            id++
        }
        functionsList = newFunctionsList
    }

    private fun parseBlock(block: BlockImpl): Block {
        when (block.blockName) {
            "assignmentBlock" -> {
                val newBlock = AssignmentBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked
                )
                newBlock.variableName = block.variableName
                newBlock.newValue = block.newValue

                return newBlock
            }

            "beginBlock" -> {
                return BeginBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked
                )
            }

            "breakBlock" -> {
                return BreakBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked
                )
            }

            "callFunctionBlock" -> {
                val newBlock = CallFunctionBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked
                )

                newBlock.functionName = block.functionName
                newBlock.arguments = block.arguments

                return newBlock
            }

            "continueBlock" -> {
                return ContinueBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )
            }

            "elseBlock" -> {
                return ElseBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                    beginKey = block.beginKey,
                    endKey = block.endKey
                )
            }

            "endBlock" -> {
                return EndBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )
            }

            "finishProgram" -> {
                return FinishProgramBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )
            }

            "functionNameBlock" -> {
                val newBlock = FunctionNameBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )

                newBlock.functionName = block.functionName
                return newBlock
            }

            "functionsArgumentBlock" -> {
                val newBlock = FunctionsArgumentBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )

                newBlock.parameters = block.parameters
                return newBlock
            }

            "ifBlock" -> {
                val newBlock = IfBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                    beginKey = block.beginKey,
                    endKey = block.endKey
                )

                newBlock.condition = block.condition
                return newBlock
            }

            "initArrayBlock" -> {
                val newBlock = InitializeArrayBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )

                newBlock.arrayName = block.arrayName
                newBlock.arrayType = block.arrayType
                newBlock.arraySize = block.arraySize
                return newBlock
            }

            "initVarBlock" -> {
                val newBlock = InitializeVarBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )

                newBlock.name = block.name
                newBlock.type = block.type
                newBlock.value = block.value
                return newBlock
            }

            "mainBlock" -> {
                return MainBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )
            }

            "outputBlock" -> {
                val newBlock = OutputBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )

                newBlock.expression = block.expression
                return newBlock
            }

            "returnBlock" -> {
                return ReturnBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                )
            }

            "whileBlock" -> {
                val newBlock = WhileBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked,
                    beginKey = block.beginKey,
                    endKey = block.endKey
                )

                newBlock.condition = block.condition
                newBlock.conditionText = block.conditionText
                return newBlock
            }

            "inputBlock" -> {
                val newBlock = InputBlock(
                    key = block.key,
                    title = block.title,
                    isDragOverLocked = block.isDragOverLocked
                )

                newBlock.variableName = block.variableName
                newBlock.newValue = block.newValue
                return newBlock

            }
        }
        return AssignmentBlock(
            key = block.key,
            title = block.title,
            isDragOverLocked = block.isDragOverLocked
        )
    }
}