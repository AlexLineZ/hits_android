package com.example.hits_android.appmodel.data.filestorage

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.hits_android.blocks.Block
import com.example.hits_android.blocks.FunctionClass
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
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

    suspend fun loadFunctionListFromJson(fileName: String): List<List<Block>> {
        return withContext(Dispatchers.Main) {
            val fileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val jsonString = bufferedReader.use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(Block::class.java, BlockInstanceCreator())
                .create()

            val arrayType = object : TypeToken<List<List<Block>>>() {}.type

            val functionList: List<List<Block>> = gson.fromJson(jsonString, arrayType)

            Log.d("a", "${functionList[0][1]}")

            fileInputStream.close()

            return@withContext functionList
        }
    }
}

class BlockImpl(
    override val blockName: String,
    override var key: String,
    override val title: String,
    override val isDragOverLocked: Boolean
) : Block {
    override fun runCodeBlock() {
        // Реализация метода runCodeBlock()
    }
    val beginKey: String = ""
    val endKey: String = ""
    override fun getNameOfBlock(): String {
        // Реализация метода getNameOfBlock()
        return ""
    }

    @Composable
    override fun BlockComposable(item: Block) {
        // Реализация метода BlockComposable()
    }

    var condition: String = ""
    var conditionText: String = ""
}

class BlockInstanceCreator : InstanceCreator<Block> {
    override fun createInstance(type: Type): Block {
        return BlockImpl("", "", "", false)
    }
}

