package com.adrian.monuver.feature.budgeting.presentation.budgetDetail

internal sealed interface BudgetDetailAction {
    data object NavigateBack : BudgetDetailAction
    data class  NavigateToEditBudget(val budgetId: Long) : BudgetDetailAction
    data class  RemoveCurrentBudget(val budgetId: Long) : BudgetDetailAction
    data class  NavigateToTransactionDetail(val transactionId: Long) : BudgetDetailAction
}