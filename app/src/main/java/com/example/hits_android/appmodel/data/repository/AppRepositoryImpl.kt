package com.example.hits_android.appmodel.data.repository

import android.content.SharedPreferences

class AppRepositoryImpl(
    private val prefs: SharedPreferences
) {

    private val firstLaunchKey = "FIRST_LAUNCH_KEY"

    fun isFirstLaunch(): Boolean {
        val firstLaunch = prefs.getBoolean(firstLaunchKey, true)
        if(firstLaunch) {
            prefs.edit().putBoolean(firstLaunchKey, false).apply()
        }
        return firstLaunch
    }



}