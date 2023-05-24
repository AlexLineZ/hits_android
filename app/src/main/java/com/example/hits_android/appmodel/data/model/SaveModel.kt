package com.example.hits_android.appmodel.data.model

import com.example.hits_android.blocks.FunctionClass

data class SaveModel(
    val functionsList: List<FunctionClass>,
    val name: String,
    val date: Long
)
