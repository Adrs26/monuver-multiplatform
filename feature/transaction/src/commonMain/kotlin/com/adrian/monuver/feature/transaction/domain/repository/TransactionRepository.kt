package com.adrian.monuver.feature.transaction.domain.repository

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.common.BudgetStatusState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun createIncomeTransaction(transaction: Transaction)

    suspend fun createExpenseTransaction(transaction: Transaction)

    suspend fun createTransferTransaction(transaction: Transaction)

    suspend fun getBudgetUsagePercentage(category: Int): Float

    suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long)

    suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    )

    suspend fun updateIncomeTransaction(transaction: Transaction, initialAmount: Long)

    suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    )

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>>

    fun getTransactionById(transactionId: Long): Flow<Transaction?>
}