package com.adrian.monuver.feature.budgeting.presentation.editBudget

internal data class EditBudgetState(
    val id: Long = 0,
    val category: Int = 0,
    val maxAmount: Long = 0,
    val cycle: Int = 0,
    val startDate: String = "",
    val endDate: String = "",
    val isOverflowAllowed: Boolean = false,
    val isAutoUpdate: Boolean = false
)
