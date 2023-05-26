package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.hits_android.blocks.Block
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

class SaveFileStorage(private val context: Context) {

    suspend fun saveFunctionListToJson(functionList: List<List<Block>>, fileName: String) {
        withContext(Dispatchers.Main) {
            val gson = Gson()

            val jsonString = gson.toJson(functionList)


            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        }
    }

    suspend fun loadFunctionListFromJson(fileName: String): List<List<BlockImpl>> {
        return withContext(Dispatchers.Main) {
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

class BlockImpl : Block {
    override fun runCodeBlock() {
        // Реализация метода runCodeBlock()
    }

    override fun getNameOfBlock(): String {
        // Реализация метода getNameOfBlock()
        return ""
    }

    @Composable
    override fun BlockComposable(item: Block) {
        // Реализация метода BlockComposable()
    }

    override var blockName: String = ""
    override var key: String = ""
    override var title: String = ""
    override var isDragOverLocked: Boolean = false

    var condition: String = ""
    var conditionText: String = ""
    val beginKey: String = ""
    val endKey: String = ""

    var expression: String = ""

    var name: String = ""
    var type: String = ""
    var value: String = ""

    var arrayName: String = ""
    var arrayType: String = ""
    var arraySize: String = ""

    var parameters: String = ""

    var functionName: String = ""
    var arguments: String = ""

    var variableName: String = ""
    var newValue: String = ""
}

class BlockInstanceCreator : InstanceCreator<Block> {
    override fun createInstance(type: Type): Block {
        return BlockImpl()
    }
}

