package com.adrian.monuver.feature.budgeting.domain.model

internal data class AddBudget(
    val category: Int,
    val maxAmount: Long,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)