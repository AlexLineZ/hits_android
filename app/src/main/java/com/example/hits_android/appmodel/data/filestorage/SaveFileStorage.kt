package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import androidx.annotation.Keep
import com.example.hits_android.blocks.FunctionClass
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

class SaveFileStorage(private val context: Context) {

    suspend fun saveFunctionListToJson(functionList: List<FunctionClass>, fileName: String) {
        withContext(Dispatchers.Main){
            val a = A().apply {
                this.a = functionList
            }
            val gson = Gson()
            val jsonString = gson.toJson(a)

            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        }
    }

    suspend fun loadFunctionListFromJson(fileName: String): List<FunctionClass> {
        return withContext(Dispatchers.Main){
            val fileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val jsonString = bufferedReader.use { it.readText() }

            val gson = Gson()
            val functionList: A = gson.fromJson(jsonString, A::class.java)

            fileInputStream.close()

            return@withContext functionList.a
        }
    }
}

private class A() : Serializable{
    var a: List<FunctionClass> = emptyList()
}