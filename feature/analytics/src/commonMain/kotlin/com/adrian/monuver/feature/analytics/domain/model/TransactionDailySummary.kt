package com.adrian.monuver.feature.analytics.domain.model

internal data class TransactionDailySummary(
    val date: String,
    val totalIncome: Long,
    val totalExpense: Long
)