package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

internal class GetTransactionsByCategoryAndDateRangeUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>> {
        return repository.getTransactionsByParentCategoryAndDateRange(
            category = category,
            startDate = startDate,
            endDate = endDate
        )
    }
}