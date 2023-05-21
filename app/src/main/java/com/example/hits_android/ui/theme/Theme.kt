package com.example.hits_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val lightGreenColors = lightColorScheme(
    primary = md_theme_lightGreen_primary,
    onPrimary = md_theme_lightGreen_onPrimary,
    primaryContainer = md_theme_lightGreen_primaryContainer,
    onPrimaryContainer = md_theme_lightGreen_onPrimaryContainer,
    secondary = md_theme_lightGreen_secondary,
    onSecondary = md_theme_lightGreen_onSecondary,
    secondaryContainer = md_theme_lightGreen_secondaryContainer,
    onSecondaryContainer = md_theme_lightGreen_onSecondaryContainer,
    tertiary = md_theme_lightGreen_tertiary,
    onTertiary = md_theme_lightGreen_onTertiary,
    tertiaryContainer = md_theme_lightGreen_tertiaryContainer,
    onTertiaryContainer = md_theme_lightGreen_onTertiaryContainer,
    error = md_theme_lightGreen_error,
    errorContainer = md_theme_lightGreen_errorContainer,
    onError = md_theme_lightGreen_onError,
    onErrorContainer = md_theme_lightGreen_onErrorContainer,
    background = md_theme_lightGreen_background,
    onBackground = md_theme_lightGreen_onBackground,
    surface = md_theme_lightGreen_surface,
    onSurface = md_theme_lightGreen_onSurface,
    surfaceVariant = md_theme_lightGreen_surfaceVariant,
    onSurfaceVariant = md_theme_lightGreen_onSurfaceVariant,
    outline = md_theme_lightGreen_outline,
    inverseOnSurface = md_theme_lightGreen_inverseOnSurface,
    inverseSurface = md_theme_lightGreen_inverseSurface,
    inversePrimary = md_theme_lightGreen_inversePrimary,
    surfaceTint = md_theme_lightGreen_surfaceTint,
    outlineVariant = md_theme_lightGreen_outlineVariant,
    scrim = md_theme_lightGreen_scrim,
)


private val darkGreenColors = darkColorScheme(
    primary = md_theme_darkGreen_primary,
    onPrimary = md_theme_darkGreen_onPrimary,
    primaryContainer = md_theme_darkGreen_primaryContainer,
    onPrimaryContainer = md_theme_darkGreen_onPrimaryContainer,
    secondary = md_theme_darkGreen_secondary,
    onSecondary = md_theme_darkGreen_onSecondary,
    secondaryContainer = md_theme_darkGreen_secondaryContainer,
    onSecondaryContainer = md_theme_darkGreen_onSecondaryContainer,
    tertiary = md_theme_darkGreen_tertiary,
    onTertiary = md_theme_darkGreen_onTertiary,
    tertiaryContainer = md_theme_darkGreen_tertiaryContainer,
    onTertiaryContainer = md_theme_darkGreen_onTertiaryContainer,
    error = md_theme_darkGreen_error,
    errorContainer = md_theme_darkGreen_errorContainer,
    onError = md_theme_darkGreen_onError,
    onErrorContainer = md_theme_darkGreen_onErrorContainer,
    background = md_theme_darkGreen_background,
    onBackground = md_theme_darkGreen_onBackground,
    surface = md_theme_darkGreen_surface,
    onSurface = md_theme_darkGreen_onSurface,
    surfaceVariant = md_theme_darkGreen_surfaceVariant,
    onSurfaceVariant = md_theme_darkGreen_onSurfaceVariant,
    outline = md_theme_darkGreen_outline,
    inverseOnSurface = md_theme_darkGreen_inverseOnSurface,
    inverseSurface = md_theme_darkGreen_inverseSurface,
    inversePrimary = md_theme_darkGreen_inversePrimary,
    surfaceTint = md_theme_darkGreen_surfaceTint,
    outlineVariant = md_theme_darkGreen_outlineVariant,
    scrim = md_theme_darkGreen_scrim,
)

private val lightPurpleColors = lightColorScheme(
    primary = md_theme_lightPurple_primary,
    onPrimary = md_theme_lightPurple_onPrimary,
    primaryContainer = md_theme_lightPurple_primaryContainer,
    onPrimaryContainer = md_theme_lightPurple_onPrimaryContainer,
    secondary = md_theme_lightPurple_secondary,
    onSecondary = md_theme_lightPurple_onSecondary,
    secondaryContainer = md_theme_lightPurple_secondaryContainer,
    onSecondaryContainer = md_theme_lightPurple_onSecondaryContainer,
    tertiary = md_theme_lightPurple_tertiary,
    onTertiary = md_theme_lightPurple_onTertiary,
    tertiaryContainer = md_theme_lightPurple_tertiaryContainer,
    onTertiaryContainer = md_theme_lightPurple_onTertiaryContainer,
    error = md_theme_lightPurple_error,
    errorContainer = md_theme_lightPurple_errorContainer,
    onError = md_theme_lightPurple_onError,
    onErrorContainer = md_theme_lightPurple_onErrorContainer,
    background = md_theme_lightPurple_background,
    onBackground = md_theme_lightPurple_onBackground,
    surface = md_theme_lightPurple_surface,
    onSurface = md_theme_lightPurple_onSurface,
    surfaceVariant = md_theme_lightPurple_surfaceVariant,
    onSurfaceVariant = md_theme_lightPurple_onSurfaceVariant,
    outline = md_theme_lightPurple_outline,
    inverseOnSurface = md_theme_lightPurple_inverseOnSurface,
    inverseSurface = md_theme_lightPurple_inverseSurface,
    inversePrimary = md_theme_lightPurple_inversePrimary,
    surfaceTint = md_theme_lightPurple_surfaceTint,
    outlineVariant = md_theme_lightPurple_outlineVariant,
    scrim = md_theme_lightPurple_scrim,
)


private val darkPurpleColors = darkColorScheme(
    primary = md_theme_darkPurple_primary,
    onPrimary = md_theme_darkPurple_onPrimary,
    primaryContainer = md_theme_darkPurple_primaryContainer,
    onPrimaryContainer = md_theme_darkPurple_onPrimaryContainer,
    secondary = md_theme_darkPurple_secondary,
    onSecondary = md_theme_darkPurple_onSecondary,
    secondaryContainer = md_theme_darkPurple_secondaryContainer,
    onSecondaryContainer = md_theme_darkPurple_onSecondaryContainer,
    tertiary = md_theme_darkPurple_tertiary,
    onTertiary = md_theme_darkPurple_onTertiary,
    tertiaryContainer = md_theme_darkPurple_tertiaryContainer,
    onTertiaryContainer = md_theme_darkPurple_onTertiaryContainer,
    error = md_theme_darkPurple_error,
    errorContainer = md_theme_darkPurple_errorContainer,
    onError = md_theme_darkPurple_onError,
    onErrorContainer = md_theme_darkPurple_onErrorContainer,
    background = md_theme_darkPurple_background,
    onBackground = md_theme_darkPurple_onBackground,
    surface = md_theme_darkPurple_surface,
    onSurface = md_theme_darkPurple_onSurface,
    surfaceVariant = md_theme_darkPurple_surfaceVariant,
    onSurfaceVariant = md_theme_darkPurple_onSurfaceVariant,
    outline = md_theme_darkPurple_outline,
    inverseOnSurface = md_theme_darkPurple_inverseOnSurface,
    inverseSurface = md_theme_darkPurple_inverseSurface,
    inversePrimary = md_theme_darkPurple_inversePrimary,
    surfaceTint = md_theme_darkPurple_surfaceTint,
    outlineVariant = md_theme_darkPurple_outlineVariant,
    scrim = md_theme_darkPurple_scrim,
)


private val lightPinkColors = lightColorScheme(
    primary = md_theme_lightPink_primary,
    onPrimary = md_theme_lightPink_onPrimary,
    primaryContainer = md_theme_lightPink_primaryContainer,
    onPrimaryContainer = md_theme_lightPink_onPrimaryContainer,
    secondary = md_theme_lightPink_secondary,
    onSecondary = md_theme_lightPink_onSecondary,
    secondaryContainer = md_theme_lightPink_secondaryContainer,
    onSecondaryContainer = md_theme_lightPink_onSecondaryContainer,
    tertiary = md_theme_lightPink_tertiary,
    onTertiary = md_theme_lightPink_onTertiary,
    tertiaryContainer = md_theme_lightPink_tertiaryContainer,
    onTertiaryContainer = md_theme_lightPink_onTertiaryContainer,
    error = md_theme_lightPink_error,
    errorContainer = md_theme_lightPink_errorContainer,
    onError = md_theme_lightPink_onError,
    onErrorContainer = md_theme_lightPink_onErrorContainer,
    background = md_theme_lightPink_background,
    onBackground = md_theme_lightPink_onBackground,
    surface = md_theme_lightPink_surface,
    onSurface = md_theme_lightPink_onSurface,
    surfaceVariant = md_theme_lightPink_surfaceVariant,
    onSurfaceVariant = md_theme_lightPink_onSurfaceVariant,
    outline = md_theme_lightPink_outline,
    inverseOnSurface = md_theme_lightPink_inverseOnSurface,
    inverseSurface = md_theme_lightPink_inverseSurface,
    inversePrimary = md_theme_lightPink_inversePrimary,
    surfaceTint = md_theme_lightPink_surfaceTint,
    outlineVariant = md_theme_lightPink_outlineVariant,
    scrim = md_theme_lightPink_scrim,
)


private val darkPinkColors = darkColorScheme(
    primary = md_theme_darkPink_primary,
    onPrimary = md_theme_darkPink_onPrimary,
    primaryContainer = md_theme_darkPink_primaryContainer,
    onPrimaryContainer = md_theme_darkPink_onPrimaryContainer,
    secondary = md_theme_darkPink_secondary,
    onSecondary = md_theme_darkPink_onSecondary,
    secondaryContainer = md_theme_darkPink_secondaryContainer,
    onSecondaryContainer = md_theme_darkPink_onSecondaryContainer,
    tertiary = md_theme_darkPink_tertiary,
    onTertiary = md_theme_darkPink_onTertiary,
    tertiaryContainer = md_theme_darkPink_tertiaryContainer,
    onTertiaryContainer = md_theme_darkPink_onTertiaryContainer,
    error = md_theme_darkPink_error,
    errorContainer = md_theme_darkPink_errorContainer,
    onError = md_theme_darkPink_onError,
    onErrorContainer = md_theme_darkPink_onErrorContainer,
    background = md_theme_darkPink_background,
    onBackground = md_theme_darkPink_onBackground,
    surface = md_theme_darkPink_surface,
    onSurface = md_theme_darkPink_onSurface,
    surfaceVariant = md_theme_darkPink_surfaceVariant,
    onSurfaceVariant = md_theme_darkPink_onSurfaceVariant,
    outline = md_theme_darkPink_outline,
    inverseOnSurface = md_theme_darkPink_inverseOnSurface,
    inverseSurface = md_theme_darkPink_inverseSurface,
    inversePrimary = md_theme_darkPink_inversePrimary,
    surfaceTint = md_theme_darkPink_surfaceTint,
    outlineVariant = md_theme_darkPink_outlineVariant,
    scrim = md_theme_darkPink_scrim,
)





private val lightBlueColors = lightColorScheme(
    primary = md_theme_lightBlue_primary,
    onPrimary = md_theme_lightBlue_onPrimary,
    primaryContainer = md_theme_lightBlue_primaryContainer,
    onPrimaryContainer = md_theme_lightBlue_onPrimaryContainer,
    secondary = md_theme_lightBlue_secondary,
    onSecondary = md_theme_lightBlue_onSecondary,
    secondaryContainer = md_theme_lightBlue_secondaryContainer,
    onSecondaryContainer = md_theme_lightBlue_onSecondaryContainer,
    tertiary = md_theme_lightBlue_tertiary,
    onTertiary = md_theme_lightBlue_onTertiary,
    tertiaryContainer = md_theme_lightBlue_tertiaryContainer,
    onTertiaryContainer = md_theme_lightBlue_onTertiaryContainer,
    error = md_theme_lightBlue_error,
    errorContainer = md_theme_lightBlue_errorContainer,
    onError = md_theme_lightBlue_onError,
    onErrorContainer = md_theme_lightBlue_onErrorContainer,
    background = md_theme_lightBlue_background,
    onBackground = md_theme_lightBlue_onBackground,
    surface = md_theme_lightBlue_surface,
    onSurface = md_theme_lightBlue_onSurface,
    surfaceVariant = md_theme_lightBlue_surfaceVariant,
    onSurfaceVariant = md_theme_lightBlue_onSurfaceVariant,
    outline = md_theme_lightBlue_outline,
    inverseOnSurface = md_theme_lightBlue_inverseOnSurface,
    inverseSurface = md_theme_lightBlue_inverseSurface,
    inversePrimary = md_theme_lightBlue_inversePrimary,
    surfaceTint = md_theme_lightBlue_surfaceTint,
    outlineVariant = md_theme_lightBlue_outlineVariant,
    scrim = md_theme_lightBlue_scrim,
)


private val darkBlueColors = darkColorScheme(
    primary = md_theme_darkBlue_primary,
    onPrimary = md_theme_darkBlue_onPrimary,
    primaryContainer = md_theme_darkBlue_primaryContainer,
    onPrimaryContainer = md_theme_darkBlue_onPrimaryContainer,
    secondary = md_theme_darkBlue_secondary,
    onSecondary = md_theme_darkBlue_onSecondary,
    secondaryContainer = md_theme_darkBlue_secondaryContainer,
    onSecondaryContainer = md_theme_darkBlue_onSecondaryContainer,
    tertiary = md_theme_darkBlue_tertiary,
    onTertiary = md_theme_darkBlue_onTertiary,
    tertiaryContainer = md_theme_darkBlue_tertiaryContainer,
    onTertiaryContainer = md_theme_darkBlue_onTertiaryContainer,
    error = md_theme_darkBlue_error,
    errorContainer = md_theme_darkBlue_errorContainer,
    onError = md_theme_darkBlue_onError,
    onErrorContainer = md_theme_darkBlue_onErrorContainer,
    background = md_theme_darkBlue_background,
    onBackground = md_theme_darkBlue_onBackground,
    surface = md_theme_darkBlue_surface,
    onSurface = md_theme_darkBlue_onSurface,
    surfaceVariant = md_theme_darkBlue_surfaceVariant,
    onSurfaceVariant = md_theme_darkBlue_onSurfaceVariant,
    outline = md_theme_darkBlue_outline,
    inverseOnSurface = md_theme_darkBlue_inverseOnSurface,
    inverseSurface = md_theme_darkBlue_inverseSurface,
    inversePrimary = md_theme_darkBlue_inversePrimary,
    surfaceTint = md_theme_darkBlue_surfaceTint,
    outlineVariant = md_theme_darkBlue_outlineVariant,
    scrim = md_theme_darkBlue_scrim,
)

@Composable
fun Hits_androidTheme(
    selectedTheme: AppTheme = if (isSystemInDarkTheme()) AppTheme.DarkGreen else AppTheme.LightGreen,
    content: @Composable() () -> Unit
) {

    val colors = when (selectedTheme) {
        AppTheme.LightGreen -> lightGreenColors
        AppTheme.DarkGreen -> darkGreenColors
        AppTheme.LightPurple -> lightPurpleColors
        AppTheme.DarkPurple -> darkPurpleColors
        AppTheme.LightPink -> lightPinkColors
        AppTheme.DarkPink -> darkPinkColors
        AppTheme.LightBlue -> lightBlueColors
        AppTheme.DarkBlue -> darkBlueColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}


@Composable
fun BuildTheme(theme: Pair<AppThemeBrightness, AppThemeColor>): AppTheme {
    return when (theme.first) {
        AppThemeBrightness.Light -> {
            when (theme.second) {
                AppThemeColor.Green -> AppTheme.LightGreen
                AppThemeColor.Purple -> AppTheme.LightPurple
                AppThemeColor.Pink -> AppTheme.LightPink
                AppThemeColor.Blue -> AppTheme.LightBlue
            }
        }

        AppThemeBrightness.Dark -> {
            when (theme.second) {
                AppThemeColor.Green -> AppTheme.DarkGreen
                AppThemeColor.Purple -> AppTheme.DarkPurple
                AppThemeColor.Pink -> AppTheme.DarkPink
                AppThemeColor.Blue -> AppTheme.DarkBlue
            }
        }

        AppThemeBrightness.System -> {
            when (isSystemInDarkTheme()) {
                true -> when (theme.second) {
                    AppThemeColor.Green -> AppTheme.DarkGreen
                    AppThemeColor.Purple -> AppTheme.DarkPurple
                    AppThemeColor.Pink -> AppTheme.DarkPink
                    AppThemeColor.Blue -> AppTheme.DarkBlue
                }

                false -> when (theme.second) {
                    AppThemeColor.Green -> AppTheme.LightGreen
                    AppThemeColor.Purple -> AppTheme.LightPurple
                    AppThemeColor.Pink -> AppTheme.LightPink
                    AppThemeColor.Blue -> AppTheme.LightBlue
                }
            }
        }
    }
}

enum class AppThemeColor {
    Green,
    Purple,
    Pink,
    Blue
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
    DarkBlue
}