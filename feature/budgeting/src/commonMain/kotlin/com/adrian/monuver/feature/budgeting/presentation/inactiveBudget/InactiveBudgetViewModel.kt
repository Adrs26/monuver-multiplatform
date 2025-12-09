package com.adrian.monuver.feature.budgeting.presentation.inactiveBudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem
import com.adrian.monuver.feature.budgeting.domain.usecase.GetAllInactiveBudgetsUseCase
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class InactiveBudgetViewModel(
    getAllInactiveBudgetsUseCase: GetAllInactiveBudgetsUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val budgets = getAllInactiveBudgetsUseCase(viewModelScope)
        .map { pagingData ->
            pagingData.map { budget ->
                BudgetItem(
                    id = budget.id,
                    category = budget.category,
                    startDate = budget.startDate,
                    endDate = budget.endDate,
                    maxAmount = budget.maxAmount,
                    usedAmount = budget.usedAmount
                )
            }
        }.flowOn(customDispatcher.default)
}