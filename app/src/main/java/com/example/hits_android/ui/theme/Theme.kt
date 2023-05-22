package com.example.hits_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Hits_androidTheme(
    selectedTheme: AppTheme = if (isSystemInDarkTheme()) AppTheme.DarkGreen else AppTheme.LightGreen,
    content: @Composable() () -> Unit
) {

    val colors = when (selectedTheme) {
        AppTheme.LightGreen -> ThemeColors.LightGreenColors
        AppTheme.DarkGreen -> ThemeColors.DarkGreenColors
        AppTheme.LightPurple -> ThemeColors.LightPurpleColors
        AppTheme.DarkPurple -> ThemeColors.DarkPurpleColors
        AppTheme.LightPink -> ThemeColors.LightPinkColors
        AppTheme.DarkPink -> ThemeColors.DarkPinkColors
        AppTheme.LightBlue -> ThemeColors.LightBlueColors
        AppTheme.DarkBlue -> ThemeColors.DarkBlueColors
        AppTheme.LightRed -> ThemeColors.LightRedColors
        AppTheme.DarkRed -> ThemeColors.DarkRedColors
        AppTheme.LightYellow -> ThemeColors.LightYellowColors
        AppTheme.DarkYellow -> ThemeColors.DarkYellowColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )
}


@Composable
fun buildTheme(theme: Pair<AppThemeBrightness, AppThemeColor>): AppTheme {
    return when (theme.first) {
        AppThemeBrightness.Light -> {
            when (theme.second) {
                AppThemeColor.Green -> AppTheme.LightGreen
                AppThemeColor.Purple -> AppTheme.LightPurple
                AppThemeColor.Pink -> AppTheme.LightPink
                AppThemeColor.Blue -> AppTheme.LightBlue
                AppThemeColor.Red -> AppTheme.LightRed
                AppThemeColor.Yellow -> AppTheme.LightYellow
            }
        }

        AppThemeBrightness.Dark -> {
            when (theme.second) {
                AppThemeColor.Green -> AppTheme.DarkGreen
                AppThemeColor.Purple -> AppTheme.DarkPurple
                AppThemeColor.Pink -> AppTheme.DarkPink
                AppThemeColor.Blue -> AppTheme.DarkBlue
                AppThemeColor.Red -> AppTheme.DarkRed
                AppThemeColor.Yellow -> AppTheme.DarkYellow
            }
        }

        AppThemeBrightness.System -> {
            when (isSystemInDarkTheme()) {
                true -> when (theme.second) {
                    AppThemeColor.Green -> AppTheme.DarkGreen
                    AppThemeColor.Purple -> AppTheme.DarkPurple
                    AppThemeColor.Pink -> AppTheme.DarkPink
                    AppThemeColor.Blue -> AppTheme.DarkBlue
                    AppThemeColor.Red -> AppTheme.DarkRed
                    AppThemeColor.Yellow -> AppTheme.DarkYellow
                }

                false -> when (theme.second) {
                    AppThemeColor.Green -> AppTheme.LightGreen
                    AppThemeColor.Purple -> AppTheme.LightPurple
                    AppThemeColor.Pink -> AppTheme.LightPink
                    AppThemeColor.Blue -> AppTheme.LightBlue
                    AppThemeColor.Red -> AppTheme.LightRed
                    AppThemeColor.Yellow -> AppTheme.LightYellow
                }
            }
        }
    }
}

enum class AppThemeColor {
    Green,
    Purple,
    Pink,
    Blue,
    Red,
    Yellow
}

enum class AppThemeBrightness {
    Light,
    Dark,
    System
}

enum class AppTheme {
    LightGreen,
    DarkGreen,
    LightPurple,
    DarkPurple,
    LightPink,
    DarkPink,
    LightBlue,
    DarkBlue,
    LightRed,
    DarkRed,
    LightYellow,
    DarkYellow
}