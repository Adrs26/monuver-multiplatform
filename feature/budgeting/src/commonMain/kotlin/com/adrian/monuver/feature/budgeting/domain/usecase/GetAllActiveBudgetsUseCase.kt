package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllActiveBudgetsUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<List<Budget>> {
        return repository.getAllActiveBudgets()
    }
}