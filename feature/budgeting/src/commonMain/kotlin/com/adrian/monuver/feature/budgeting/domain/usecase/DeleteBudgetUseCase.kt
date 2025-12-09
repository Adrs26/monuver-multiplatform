package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository

internal class DeleteBudgetUseCase(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBudgetById(id)
    }
}