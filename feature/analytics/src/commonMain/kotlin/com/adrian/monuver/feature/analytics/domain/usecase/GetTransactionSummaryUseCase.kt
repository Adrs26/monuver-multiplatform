package com.adrian.monuver.feature.analytics.domain.usecase

import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.analytics.domain.model.TransactionDailySummary
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal class GetTransactionSummaryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(month: Int, year: Int, week: Int): Flow<List<TransactionDailySummary>> {
        val (startDate, endDate) = DateHelper.getDateRangeForWeek(week, month, year)

        val transactionsFlow = repository.getTransactionsInRange(
            startDate.toString(),
            endDate.toString()
        )

        return transactionsFlow.map { transactions ->
            val grouped = transactions.groupBy { LocalDate.parse(it.date) }

            val result = mutableListOf<TransactionDailySummary>()

            var currentDate = startDate
            while (currentDate <= endDate) {
                val currentDateTransactions = grouped[currentDate] ?: emptyList()

                val (income, expense) = currentDateTransactions.fold(0L to 0L) { acc, transaction ->
                    when (transaction.type) {
                        TransactionType.INCOME -> acc.copy(first = acc.first + transaction.amount)
                        TransactionType.EXPENSE -> acc.copy(second = acc.second + transaction.amount)
                        else -> acc
                    }
                }

                result.add(
                    TransactionDailySummary(
                        date = currentDate.toString(),
                        totalIncome = income,
                        totalExpense = expense
                    )
                )

                currentDate = currentDate.plus(DatePeriod(days = 1))
            }

            result
        }
    }
}