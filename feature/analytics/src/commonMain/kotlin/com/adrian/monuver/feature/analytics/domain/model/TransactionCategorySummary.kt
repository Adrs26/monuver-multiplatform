package com.adrian.monuver.feature.analytics.domain.model

internal data class TransactionCategorySummary(
    val parentCategory: Int,
    val totalAmount: Long
)