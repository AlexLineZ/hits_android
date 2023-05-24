package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import com.example.hits_android.blocks.FunctionClass
import com.google.gson.Gson
import java.io.File

class SaveFileStorage(private val context: Context) {
    fun loadFileList(filePath: String): List<FunctionClass>{
        val gson = Gson()
        val jsonString = File(filePath).readText()

        return gson.fromJson(jsonString, Array<FunctionClass>::class.java).toList()
    }

    fun saveFileList(functionList: List<FunctionClass>, filePath: String) {
        val gson = Gson()
        val jsonString = gson.toJson(functionList)

        val file = File(filePath)
        file.writeText(jsonString)
    }

}