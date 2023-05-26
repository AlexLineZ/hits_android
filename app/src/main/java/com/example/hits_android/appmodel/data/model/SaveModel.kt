package com.example.hits_android.appmodel.data.model

import com.example.hits_android.appmodel.data.filestorage.BlockImpl

data class SaveModel(
    val functionsList: List<List<BlockImpl>>,
    var name: String,
    val date: Long
)
