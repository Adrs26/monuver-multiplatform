package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

internal class GetBudgetByIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(id: Long): Flow<Budget?> {
        return repository.getBudgetById(id)
    }
}