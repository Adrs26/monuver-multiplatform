package com.adrian.monuver.feature.analytics.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllTransactionsByCategoryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<Transaction>> {
        return repository.getTransactionsByParentCategoryAndMonthAndYear(
            category = category,
            month = month,
            year = year
        )
    }
}