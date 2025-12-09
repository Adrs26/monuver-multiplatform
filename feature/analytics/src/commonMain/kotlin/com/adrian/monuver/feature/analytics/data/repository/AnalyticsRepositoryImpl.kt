package com.adrian.monuver.feature.analytics.data.repository

import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.entity.projection.TransactionCategorySummaryEntity
import com.adrian.monuver.core.data.database.entity.projection.TransactionSummaryEntity
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.analytics.domain.model.TransactionCategorySummary
import com.adrian.monuver.feature.analytics.domain.model.TransactionSummary
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AnalyticsRepositoryImpl(
    private val transactionDao: TransactionDao
) : AnalyticsRepository {

    override fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByParentCategoryAndMonthAndYear(
            category = category,
            month = month,
            year = year
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTotalMonthlyTransactionAmount(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Long?> {
        return transactionDao.getTotalMonthlyTransactionAmount(
            type = type,
            month = month,
            year = year
        )
    }

    override fun getAverageDailyTransactionAmountInMonth(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Double?> {
        return transactionDao.getAverageDailyTransactionAmountInMonth(
            type = type,
            month = month,
            year = year
        )
    }

    override fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummary>> {
        return transactionDao.getGroupedMonthlyTransactionAmountByParentCategory(
            type = type,
            month = month,
            year = year
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummary>> {
        return transactionDao.getTransactionsInRange(
            startDate = startDate,
            endDate = endDate
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    private fun TransactionSummaryEntity.toDomain() = TransactionSummary(
        type = type,
        date = date,
        amount = amount
    )

    private fun TransactionCategorySummaryEntity.toDomain() = TransactionCategorySummary(
        parentCategory = parentCategory,
        totalAmount = totalAmount
    )
}