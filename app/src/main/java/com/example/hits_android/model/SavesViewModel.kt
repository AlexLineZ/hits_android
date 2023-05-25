package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hits_android.appmodel.data.model.SaveInfoModel
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.appmodel.data.repository.SaveRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SavesViewModel(
    private val saveRepository: SaveRepository
) : ViewModel() {

    val state = MutableStateFlow(listOf<SaveInfoModel>())
    val loadedSave = MutableStateFlow<SaveModel>(SaveModel(listOf(), "---", 0))

    fun startWriteSave(saveModel: SaveModel) {
        viewModelScope.launch {
            saveRepository.createSave(saveModel)
        }
    }

    fun startGetSave(name: String) {
        viewModelScope.launch() {
            loadedSave.value = saveRepository.getSave(name)
        }
    }

    fun startGetAllSaves() {
        viewModelScope.launch(Dispatchers.Main) {
            state.value = saveRepository.getAllSaves()
        }
    }

}