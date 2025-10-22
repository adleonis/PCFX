package org.pcfx.client.c1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Color Palette
object AppColors {
    val DarkGrey = Color(0xFF1F1F1F)
    val DarkGreyAlt = Color(0xFF2A2A2A)
    val MediumGrey = Color(0xFF4A4A4A)
    val LightGrey = Color(0xFF8A8A8A)
    val VeryLightGrey = Color(0xFFE8E8E8)
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Blue = Color(0xFF1E90FF)
    val BlueDark = Color(0xFF1565C0)
    val BlueLight = Color(0xFF42A5F5)
    val Red = Color(0xFFE53935)
    val Green = Color(0xFF43A047)
    val Orange = Color(0xFFFB8C00)
}

private val darkColorScheme = darkColorScheme(
    primary = AppColors.Blue,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.BlueDark,
    onPrimaryContainer = AppColors.BlueLight,
    secondary = AppColors.MediumGrey,
    onSecondary = AppColors.White,
    background = AppColors.DarkGrey,
    onBackground = AppColors.VeryLightGrey,
    surface = AppColors.DarkGreyAlt,
    onSurface = AppColors.VeryLightGrey,
    error = AppColors.Red,
    onError = AppColors.White,
    outline = AppColors.MediumGrey
)

private val lightColorScheme = lightColorScheme(
    primary = AppColors.Blue,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.BlueDark,
    onPrimaryContainer = AppColors.BlueLight,
    secondary = AppColors.LightGrey,
    onSecondary = AppColors.White,
    background = AppColors.White,
    onBackground = AppColors.Black,
    surface = AppColors.VeryLightGrey,
    onSurface = AppColors.Black,
    error = AppColors.Red,
    onError = AppColors.White,
    outline = AppColors.LightGrey
)

@Composable
fun ClientC1Theme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
