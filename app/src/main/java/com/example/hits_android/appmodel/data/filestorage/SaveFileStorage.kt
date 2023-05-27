package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.BlockImpl
import com.example.hits_android.blocks.BlockInstanceCreator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class SaveFileStorage(private val context: Context) {

    suspend fun saveFunctionListToJson(functionList: List<List<Block>>, fileName: String) {
        withContext(Dispatchers.IO) {
            val gson = Gson()

            val jsonString = gson.toJson(functionList)


            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        }
    }

    suspend fun loadFunctionListFromJson(fileName: String): List<List<BlockImpl>> {
        return withContext(Dispatchers.IO) {
            val fileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val jsonString = bufferedReader.use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(BlockImpl::class.java, BlockInstanceCreator())
                .create()

            val arrayType = object : TypeToken<List<List<BlockImpl>>>() {}.type

            val functionList: List<List<BlockImpl>> = gson.fromJson(jsonString, arrayType)

            fileInputStream.close()

            return@withContext functionList
        }
    }
}
