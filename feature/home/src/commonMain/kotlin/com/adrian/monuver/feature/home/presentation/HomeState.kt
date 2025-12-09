package com.adrian.monuver.feature.home.presentation

import com.adrian.monuver.core.domain.model.BudgetSummary
import com.adrian.monuver.core.domain.model.TransactionItem

internal data class HomeState(
    val totalBalance: Long = 0,
    val recentTransactions: List<TransactionItem> = emptyList(),
    val budgetSummary: BudgetSummary = BudgetSummary()
)