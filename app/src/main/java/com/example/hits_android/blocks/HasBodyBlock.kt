package com.example.hits_android.blocks

// Блок, после которого следуют begin и end
interface HasBodyBlock {
    fun setFunctionList(functionList: List<FunctionClass>)
}