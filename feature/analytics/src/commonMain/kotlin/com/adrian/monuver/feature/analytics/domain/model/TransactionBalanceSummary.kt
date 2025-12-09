package com.adrian.monuver.feature.analytics.domain.model

internal data class TransactionBalanceSummary(
    val totalIncome: Long = 0,
    val totalExpense: Long = 0,
    val averageIncome: Double = 0.0,
    val averageExpense: Double = 0.0
)