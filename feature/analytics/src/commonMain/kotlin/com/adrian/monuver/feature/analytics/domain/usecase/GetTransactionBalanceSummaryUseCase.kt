package com.adrian.monuver.feature.analytics.domain.usecase

import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.analytics.domain.model.TransactionBalanceSummary
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class GetTransactionBalanceSummaryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<TransactionBalanceSummary> {
        val totalIncome = repository.getTotalMonthlyTransactionAmount(
            type = TransactionType.INCOME,
            month = month,
            year = year
        )
        val totalExpense = repository.getTotalMonthlyTransactionAmount(
            type = TransactionType.EXPENSE,
            month = month,
            year = year
        )
        val averageIncome = repository.getAverageDailyTransactionAmountInMonth(
            type = TransactionType.INCOME,
            month = month,
            year = year
        )
        val averageExpense = repository.getAverageDailyTransactionAmountInMonth(
            type = TransactionType.EXPENSE,
            month = month,
            year = year
        )

        return combine(
            totalIncome,
            totalExpense,
            averageIncome,
            averageExpense
        ) { totalIncome, totalExpense, averageIncome, averageExpense ->
            TransactionBalanceSummary(
                totalIncome = totalIncome ?: 0L,
                totalExpense = totalExpense ?: 0L,
                averageIncome = averageIncome ?: 0.0,
                averageExpense = averageExpense ?: 0.0
            )
        }
    }
}