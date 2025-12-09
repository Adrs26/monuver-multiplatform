package com.adrian.monuver.feature.analytics.domain.usecase

import com.adrian.monuver.feature.analytics.domain.model.TransactionCategorySummary
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

internal class GetTransactionCategorySummaryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummary>> {
        return repository.getGroupedMonthlyTransactionAmountByParentCategory(
            type = type,
            month = month,
            year = year
        )
    }
}