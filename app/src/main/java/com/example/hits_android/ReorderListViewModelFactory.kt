package com.example.hits_android

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hits_android.model.ReorderListViewModel
import com.example.hits_android.ui.theme.ThemePreference

class ReorderListViewModelFactory(private val sharedPreferences: ThemePreference): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReorderListViewModel(sharedPreferences) as T
    }
}