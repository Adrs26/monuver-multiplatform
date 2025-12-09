package com.adrian.monuver.feature.budgeting.presentation

internal sealed interface BudgetingAction {
    data object NavigateToInactiveBudget : BudgetingAction
    data class  NavigateToBudgetDetail(val budgetId: Long) : BudgetingAction
}