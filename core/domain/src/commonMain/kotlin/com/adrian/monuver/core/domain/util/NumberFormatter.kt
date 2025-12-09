package com.adrian.monuver.core.domain.util

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

fun Long.toDecimal(): Double {
    val factor = 10.0.pow(2)
    return this.toDouble() / factor
}

fun Long.toRupiah(trimZeroDecimals: Boolean = true): String {
    val isNegative = this < 0
    val absValue = abs(this)

    val factor = 10.0.pow(2)
    val value = absValue.toDouble() / factor

    val integerPart = value.toLong()
    val fractionalPart = ((value - integerPart) * factor).toInt()

    val formattedInt = integerPart.toString()
        .replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1.")

    val decimalPart = fractionalPart.toString().padStart(2, '0')

    val result = if (trimZeroDecimals) {
        formattedInt
    } else {
        "$formattedInt,$decimalPart"
    }

    return (if (isNegative) "-Rp" else "Rp") + result
}


fun Long.toFormattedAmount(trimZeroDecimals: Boolean = true): String {
    val factor = 10.0.pow(2)
    val value = this.toDouble() / factor

    val integerPart = value.toLong()
    val fractionalPart = ((value - integerPart) * factor).toInt()

    val formattedInt = integerPart.toString()
        .replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1.")

    val decimalPart = fractionalPart.toString().padStart(2, '0')

    return if (trimZeroDecimals) {
        formattedInt
    } else {
        "$formattedInt,$decimalPart"
    }
}

fun Double.toShortRupiah(): String {
    fun Double.clean(): String {
        val s = this.toString()
        return if (s.endsWith(".0")) s.dropLast(2) else s
    }

    return when {
        this >= 1_000_000_000 -> "Rp${(this / 1_000_000_000.0).clean()}M"
        this >= 1_000_000 -> "Rp${(this / 1_000_000.0).clean()}Jt"
        this >= 1_000 -> "Rp${(this / 1_000.0).clean()}Rb"
        else -> "Rp$this"
    }
}

fun Long.toHighestRangeValue(): Long {
    return when (this) {
        in 0L..50_000L -> 50_000L
        in 50_000L..100_000L -> 100_000L
        in 100_000L..200_000L -> 200_000L
        in 200_000L..300_000L -> 300_000L
        in 300_000L..500_000L -> 500_000L
        in 500_000L..700_000L -> 700_000L
        in 700_000L..1_000_000L -> 1_000_000L
        in 1_000_000L..3_000_000L -> 3_000_000L
        in 3_000_000L..5_000_000L -> 5_000_000L
        in 5_000_000L..7_000_000L -> 7_000_000L
        in 7_000_000L..10_000_000L -> 10_000_000L
        in 10_000_000L..15_000_000L -> 15_000_000L
        in 15_000_000L..20_000_000L -> 20_000_000L
        in 20_000_000L..30_000_000L -> 30_000_000L
        in 30_000_000L..50_000_000L -> 50_000_000L
        in 50_000_000L..100_000_000L -> 100_000_000L
        in 100_000_000L..200_000_000L -> 200_000_000L
        in 200_000_000L..500_000_000L -> 500_000_000L
        else -> 1_000_000_000L
    }
}

fun Long.percentageOf(total: Long): Long {
    if (total == 0L) return 0L
    return ((this.toDouble() / total.toDouble()) * 100).roundToLong()
}