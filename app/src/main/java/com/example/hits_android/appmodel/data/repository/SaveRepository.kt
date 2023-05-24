package com.example.hits_android.appmodel.data.repository

import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.appmodel.data.localstorage.SaveRoomStorage
import com.example.hits_android.appmodel.data.model.SaveInfoModel
import com.example.hits_android.appmodel.data.model.SaveModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SaveRepository(
    private val database: SaveRoomStorage,
    private val fileStorage: SaveFileStorage
) {

    suspend fun getSave(name: String) : SaveModel {
        val save: SaveModel

        withContext(Dispatchers.IO){
            val saveInfo = database.dao.getSave(UUID.fromString(name))
            save = SaveModel(
                name = saveInfo.name.toString(),
                date = saveInfo.date,
                functionsList = fileStorage.loadFunctionListFromJson(saveInfo.name.toString())
            )
        }
        return save
    }

    suspend fun getAllSaves() : List<SaveInfoModel>{
        val save: List<SaveInfoModel>

        withContext(Dispatchers.IO){
            save = database.dao.getAllSaves().map { SaveInfoModel(
                name = it.name.toString(),
                date = it.date
            ) }
        }

        return save
    }

    suspend fun createSave(save: SaveModel) {

    }

    suspend fun deleteSave(name: SaveInfoModel) {

    }

    suspend fun updateSave(save: SaveModel) {

    }
}