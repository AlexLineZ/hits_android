package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hits_android.appmodel.data.repository.SaveRepository
import com.example.hits_android.ui.theme.ThemePreference

class ReorderListViewModelFactory(
    private val sharedPreferences: ThemePreference,
    private val repository: SaveRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReorderListViewModel(sharedPreferences, repository) as T
    }
}