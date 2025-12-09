package com.adrian.monuver.feature.budgeting.domain.repository

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal interface BudgetRepository {

    fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<Budget>>

    fun getBudgetById(budgetId: Long): Flow<Budget?>

    suspend fun isBudgetExists(category: Int): Boolean

    suspend fun deleteBudgetById(budgetId: Long)

    suspend fun updateBudget(budget: Budget)

    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>>

    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long
}