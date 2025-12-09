package com.adrian.monuver.core.domain.model

data class Budget(
    val id: Long = 0L,
    val category: Int,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)