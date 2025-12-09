package com.adrian.monuver.feature.budgeting.presentation.addBudget

import com.adrian.monuver.feature.budgeting.domain.model.AddBudget

internal sealed interface AddBudgetAction {
    data object NavigateBack : AddBudgetAction
    data object NavigateToCategory : AddBudgetAction
    data class  AddNewBudget(val budget: AddBudget) : AddBudgetAction
}