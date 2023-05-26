package com.example.hits_android.appmodel.data.repository

import android.content.Context
import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.appmodel.data.localstorage.SaveInfoEntity
import com.example.hits_android.appmodel.data.localstorage.SaveRoomStorage
import com.example.hits_android.appmodel.data.model.SaveInfoModel
import com.example.hits_android.appmodel.data.model.SaveModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class SaveRepository(
    private val database: SaveRoomStorage,
    private val fileStorage: SaveFileStorage,
    private val context: Context
) {

    suspend fun getSave(name: String) : SaveModel {
        val save: SaveModel

        withContext(Dispatchers.IO){
            val saveInfo = database.dao.getSave(UUID.fromString(name))
            save = SaveModel(
                name = saveInfo.name.toString(),
                date = saveInfo.date,
                functionsList = fileStorage.loadFunctionListFromJson(saveInfo.name.toString()),
                realName = saveInfo.realName
            )
        }
        return save
    }

    suspend fun getAllSaves() : List<SaveInfoModel>{
        val saveList: List<SaveInfoModel>

        withContext(Dispatchers.IO){
            saveList = database.dao.getAllSaves().map { SaveInfoModel(
                name = it.name.toString(),
                date = it.date,
                realName = it.realName
            ) }
        }

        return saveList
    }

    suspend fun createSave(save: SaveModel) {
        withContext(Dispatchers.IO) {
            val saveInfoEntity = SaveInfoEntity(
                name = UUID.fromString(save.name),
                date = save.date,
                realName = save.realName
            )
            database.dao.createSave(saveInfoEntity)
            fileStorage.saveFunctionListToJson(save.functionsList, save.name)
        }
    }

    suspend fun deleteSave(name: String) {
        withContext(Dispatchers.IO) {
            val saveInfoEntity = database.dao.getSave(UUID.fromString(name))
            database.dao.deleteSave(saveInfoEntity)
            val file = File(context.filesDir, "$name.json")
            file.delete()
        }
    }
}

