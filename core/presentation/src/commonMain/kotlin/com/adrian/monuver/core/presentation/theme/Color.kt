package com.adrian.monuver.core.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val BluePrimary = Color(0xFF0077C0)
val SoftWhite = Color(0xFFF8FCFB)
val SoftBlack = Color(0xFF121212)
val LightGrayBlue = Color(0xFFE0EBF5)
val Red600 = Color(0xFFE53935)
val Orange600 = Color(0xFFFB8C00)
val Green800 = Color(0xFF2E7D32)
val Green600 = Color(0xFF43A047)
val Green400 = Color(0xFF66BB6A)
val Teal600 = Color(0xFF00897B)
val Teal400 = Color(0xFF26A69A)
val Blue800 = Color(0xFF1565C0)
val Indigo400 = Color(0xFF5C6BC0)
val BlueGrey400 = Color(0xFF78909C)
val Amber500 = Color(0xFFFFC107)
val Pink400 = Color(0xFFEC407A)
val DeepPurple400 = Color(0xFF7E57C2)
val Brown400 = Color(0xFF8D6E63)
val Blue400 = Color(0xFF42A5F5)
val Charcoal = Color(0xFF36454F)
val Green100 = Color(0xFFC8E6C9)
val Red100 = Color(0xFFFFCDD2)
val Blue100 = Color(0xFFBBDEFB)

val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = SoftWhite,
    primaryContainer = Charcoal,
    secondary = LightGrayBlue,
    background = SoftBlack,
    onBackground = SoftWhite,
    surface = SoftBlack,
    onSurface = SoftWhite,
    surfaceVariant = Color.Gray,
    onSurfaceVariant = SoftWhite.copy(alpha = 0.7f),
    outline = Color.LightGray,
    outlineVariant = Color.Gray,
    surfaceContainerHigh = SoftBlack,
    inverseSurface = SoftWhite,
    inverseOnSurface = SoftBlack,
    error = Red600
)

val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = SoftWhite,
    primaryContainer = Blue100,
    secondary = Charcoal,
    background = SoftWhite,
    onBackground = SoftBlack,
    surface = SoftWhite,
    onSurface = SoftBlack,
    surfaceVariant = Color.LightGray,
    onSurfaceVariant = SoftBlack.copy(alpha = 0.55f),
    outline = Color.Gray,
    outlineVariant = Color.LightGray,
    surfaceContainerHigh = SoftWhite,
    inverseSurface = SoftBlack,
    inverseOnSurface = SoftWhite,
    error = Red600
)