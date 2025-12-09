package com.adrian.monuver.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.inter_bold
import monuver.core.presentation.generated.resources.inter_medium
import monuver.core.presentation.generated.resources.inter_regular
import monuver.core.presentation.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun interFontFamily() = FontFamily(
    Font(Res.font.inter_regular, FontWeight.Normal),
    Font(Res.font.inter_medium, FontWeight.Medium),
    Font(Res.font.inter_semibold, FontWeight.SemiBold),
    Font(Res.font.inter_bold, FontWeight.Bold),
)

@Composable
fun interTypography() = Typography().run {
    val interFontFamily = interFontFamily()
    copy(
        titleLarge = titleLarge.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        titleMedium = titleMedium.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        titleSmall = titleSmall.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        bodyLarge = bodyLarge.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        bodyMedium = bodyMedium.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        bodySmall = bodySmall.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelLarge = labelLarge.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        headlineLarge = headlineLarge.copy(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        ),
    )
}