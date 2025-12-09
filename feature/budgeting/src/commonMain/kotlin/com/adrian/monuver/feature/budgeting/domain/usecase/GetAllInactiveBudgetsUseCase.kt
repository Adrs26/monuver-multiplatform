package com.adrian.monuver.feature.budgeting.domain.usecase

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class GetAllInactiveBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<Budget>> {
        return repository.getAllInactiveBudgets(scope)
    }
}