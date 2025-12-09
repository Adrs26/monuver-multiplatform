package com.adrian.monuver.feature.budgeting.domain.model

internal data class BudgetItem(
    val id: Long,
    val category: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
)
