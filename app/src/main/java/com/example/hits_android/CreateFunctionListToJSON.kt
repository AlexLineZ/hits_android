package com.example.hits_android

import android.util.Log
import com.example.hits_android.blocks.AssignmentBlock
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.BlockImpl
import com.example.hits_android.blocks.CallFunctionBlock
import com.example.hits_android.blocks.ElseBlock
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.blocks.FunctionNameBlock
import com.example.hits_android.blocks.FunctionsArgumentBlock
import com.example.hits_android.blocks.IfBlock
import com.example.hits_android.blocks.InitializeArrayBlock
import com.example.hits_android.blocks.InitializeVarBlock
import com.example.hits_android.blocks.InputBlock
import com.example.hits_android.blocks.OutputBlock
import com.example.hits_android.blocks.WhileBlock

fun createFunctionListToJSON(functionList: List<FunctionClass>): List<List<BlockImpl>> {
    var customMutableList: MutableList<List<BlockImpl>> = mutableListOf()

    functionList.forEach { list ->
        var newBlockList: MutableList<BlockImpl> = mutableListOf()

        list.codeBlocksList.forEach {
            newBlockList = newBlockList.toMutableList().apply {
                add(toBlockImpl(it))
            }
        }
        customMutableList = customMutableList.toMutableList().apply {
            add(newBlockList)
        }
    }

    customMutableList.forEach { list ->
        list.forEach {

            Log.d("a", it.blockName)
        }
    }

    return customMutableList
}

fun toBlockImpl(block: Block): BlockImpl {
    val newBlock = BlockImpl()
    newBlock.key = block.key
    newBlock.title = block.title
    newBlock.isDragOverLocked = block.isDragOverLocked
    newBlock.blockName = block.blockName
    when (block.blockName) {
        "assignmentBlock" -> {
            block as AssignmentBlock
            newBlock.variableName = block.variableName
            newBlock.newValue = block.newValue
        }

        "callFunctionBlock" -> {
            block as CallFunctionBlock
            newBlock.functionName = block.functionName
            newBlock.arguments = block.arguments
        }

        "elseBlock" -> {
            block as ElseBlock
            newBlock.beginKey = block.beginKey
            newBlock.endKey = block.endKey
        }

        "functionNameBlock" -> {
            block as FunctionNameBlock
            newBlock.functionName = block.functionName
        }

        "functionsArgumentBlock" -> {
            block as FunctionsArgumentBlock
            newBlock.parameters = block.parameters
        }

        "ifBlock" -> {
            block as IfBlock
            newBlock.beginKey = block.beginKey
            newBlock.endKey = block.endKey
            newBlock.condition = block.condition
        }

        "initArrayBlock" -> {
            block as InitializeArrayBlock
            newBlock.arrayName = block.arrayName
            newBlock.arrayType = block.arrayType
            newBlock.arraySize = block.arraySize
        }

        "initVarBlock" -> {
            block as InitializeVarBlock
            newBlock.name = block.name
            newBlock.type = block.type
            newBlock.value = block.value
        }

        "outputBlock" -> {
            block as OutputBlock
            newBlock.expression = block.expression
        }

        "whileBlock" -> {
            block as WhileBlock
            newBlock.beginKey = block.beginKey
            newBlock.endKey = block.endKey
            newBlock.condition = block.condition
            newBlock.conditionText = block.conditionText
        }

        "inputBlock" -> {
            block as InputBlock
            newBlock.variableName = block.variableName
            newBlock.newValue = block.newValue
        }
    }
    return newBlock
}