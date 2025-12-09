package com.adrian.monuver.feature.budgeting.presentation.editBudget

import com.adrian.monuver.feature.budgeting.domain.model.EditBudget

internal sealed interface EditBudgetAction {
    data object NavigateBack : EditBudgetAction
    data class  EditCurrentBudget(val budget: EditBudget) : EditBudgetAction
}