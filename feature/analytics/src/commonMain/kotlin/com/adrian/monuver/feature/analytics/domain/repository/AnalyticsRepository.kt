package com.adrian.monuver.feature.analytics.domain.repository

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.analytics.domain.model.TransactionCategorySummary
import com.adrian.monuver.feature.analytics.domain.model.TransactionSummary
import kotlinx.coroutines.flow.Flow

internal interface AnalyticsRepository {

    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<Transaction>>

    fun getTotalMonthlyTransactionAmount(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Long?>

    fun getAverageDailyTransactionAmountInMonth(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Double?>

    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummary>>

    fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummary>>
}