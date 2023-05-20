package com.example.hits_android.model

import androidx.lifecycle.ViewModel
import com.example.hits_android.ui.theme.MyAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

var _theme = MutableStateFlow(MyAppTheme.DarkGreen)

class ThemeViewModel() : ViewModel() {
    val theme: StateFlow<MyAppTheme> = _theme.asStateFlow()

    fun setCurrentTheme(newTheme: MyAppTheme) {
        _theme.value = newTheme
    }
}