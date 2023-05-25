package com.example.hits_android.appmodel.data.model

import com.example.hits_android.blocks.Block

data class SaveModel(
    val functionsList: List<List<Block>>,
    var name: String,
    val date: Long
)
