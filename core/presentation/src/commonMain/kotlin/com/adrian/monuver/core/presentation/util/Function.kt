package com.adrian.monuver.core.presentation.util

import androidx.compose.ui.graphics.Color
import com.adrian.monuver.core.presentation.theme.Green600
import com.adrian.monuver.core.presentation.theme.Orange600
import com.adrian.monuver.core.presentation.theme.Red600

fun calculateProgressBarValue(usedAmount: Long, maxAmount: Long): Float {
    return if (maxAmount == 0L) {
        0f
    } else {
        usedAmount.toFloat() / maxAmount.toFloat()
    }
}

fun changeProgressBarColor(usedAmount: Long, maxAmount: Long): Color {
    return when {
        usedAmount.toDouble() / maxAmount.toDouble() > 0.9 -> Red600
        usedAmount.toDouble() / maxAmount.toDouble() > 0.6 -> Orange600
        else -> Green600
    }
}