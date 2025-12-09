package com.adrian.monuver.feature.budgeting.presentation

import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem

internal data class BudgetingState(
    val totalMaxAmount: Long = 0,
    val totalUsedAmount: Long = 0,
    val budgets: List<BudgetItem> = emptyList(),
)
