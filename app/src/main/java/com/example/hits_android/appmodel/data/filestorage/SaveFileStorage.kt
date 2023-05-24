package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import com.example.hits_android.blocks.FunctionClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class SaveFileStorage(private val context: Context) {
    fun saveFunctionListToJson(functionList: List<FunctionClass>, fileName: String) {
        val gson = Gson()
        val jsonString = gson.toJson(functionList)

        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(jsonString.toByteArray())
        fileOutputStream.close()
    }

    fun loadFunctionListFromJson(fileName: String): List<FunctionClass> {
        val fileInputStream = context.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val jsonString = bufferedReader.use { it.readText() }

        val gson = Gson()
        val functionListType = object : TypeToken<List<FunctionClass>>() {}.type
        val functionList: List<FunctionClass> = gson.fromJson(jsonString, functionListType)

        fileInputStream.close()

        return functionList
    }
}