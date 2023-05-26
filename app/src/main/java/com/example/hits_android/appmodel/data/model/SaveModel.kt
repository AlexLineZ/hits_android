package com.example.hits_android.appmodel.data.model

import com.example.hits_android.appmodel.data.filestorage.BlockImpl
import java.util.UUID

data class SaveModel(
    val functionsList: List<List<BlockImpl>>,
    var name: String,
    val date: Long,
    val realName: String
)
