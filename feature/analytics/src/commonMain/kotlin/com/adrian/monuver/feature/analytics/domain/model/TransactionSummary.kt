package com.adrian.monuver.feature.analytics.domain.model

internal data class TransactionSummary(
    val type: Int,
    val date: String,
    val amount: Long
)