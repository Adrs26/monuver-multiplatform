package com.adrian.monuver.core.domain.repository

import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation
import kotlinx.coroutines.flow.Flow

interface CoreRepository {

    fun getActiveAccounts(): Flow<List<Account>>

    suspend fun getAccountBalance(accountId: Int): Long?

    fun getDistinctTransactionYears(): Flow<List<Int>>

    fun getTotalBudgetMaxAmount(): Flow<Long>

    fun getTotalBudgetUsedAmount(): Flow<Long>

    fun getAllActiveBudgets(): Flow<List<Budget>>

    fun getCategoryRecommendationByTitle(
        title: String,
        type: Int
    ): Flow<TransactionCategoryRecommendation>

    suspend fun updateBudgetStatusToInactive(category: Int)

    suspend fun createNewBudget(budget: Budget)

    suspend fun getBudgetForDate(category: Int, date: String): Budget?

    suspend fun getAllUnpaidBills(): List<Bill>
}