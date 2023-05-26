package com.example.hits_android.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ThemePreference(private val sharedPreferences: SharedPreferences) {
    private val KEY_THEME_BRIGHTNESS = "theme_brightness"
    private val KEY_THEME_COLOR = "theme_color"

    companion object{
        private const val PREFS_NAME = "theme_prefs"
        private var instance: ThemePreference? = null
        fun getInstance(context: Context): ThemePreference {
            if (instance == null){
                synchronized(this){
                    instance = ThemePreference(sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE))
                }
            }
            return instance!!
        }
    }

    fun saveThemeBrightness(brightness: AppThemeBrightness) {
        sharedPreferences.edit {
            putString(KEY_THEME_BRIGHTNESS, brightness.name)
        }
    }

    fun saveThemeColor(color: AppThemeColor) {
        sharedPreferences.edit {
            putString(KEY_THEME_COLOR, color.name)
        }
    }

    fun getSavedTheme(): Pair<AppThemeBrightness, AppThemeColor> {
        val brightness = sharedPreferences.getString(KEY_THEME_BRIGHTNESS, null)?.let {
            AppThemeBrightness.valueOf(it)
        } ?: AppThemeBrightness.System

        val color = sharedPreferences.getString(KEY_THEME_COLOR, null)?.let {
            AppThemeColor.valueOf(it)
        } ?: AppThemeColor.Green

        return Pair(brightness, color)
    }
}