package com.adrian.monuver.feature.transaction.domain.common

sealed class BudgetStatusState {
    data object NoOldBudget : BudgetStatusState()
    data object NoNewBudget : BudgetStatusState()
    data object NoBudget : BudgetStatusState()
    data object SameBudget : BudgetStatusState()
    data object DifferentBudget : BudgetStatusState()
}