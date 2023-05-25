package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.appmodel.data.repository.SaveRepository
import kotlinx.coroutines.launch

class SavesViewModel : ViewModel() {
    fun startWriteSave(saveModel: SaveModel, saveRepository: SaveRepository){
        viewModelScope.launch {
            saveRepository.createSave(saveModel)
        }
    }

    fun startGetSave(saveModel: SaveModel, saveRepository: SaveRepository){
        viewModelScope.launch {
            saveRepository.getSave()
        }
    }
}