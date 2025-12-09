package com.adrian.monuver.feature.transaction.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.BudgetDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.util.withTransaction
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.data.mapper.toEntity
import com.adrian.monuver.core.data.mapper.toEntityForUpdate
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.common.BudgetStatusState
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.absoluteValue

internal class TransactionRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun createIncomeTransaction(transaction: Transaction) {
        database.withTransaction {
            transactionDao.createNewTransaction(transaction.toEntity())
            accountDao.increaseAccountBalance(
                accountId = transaction.sourceId,
                delta = transaction.amount
            )
        }
    }

    override suspend fun createExpenseTransaction(transaction: Transaction) {
        database.withTransaction {
            transactionDao.createNewTransaction(transaction.toEntity())
            accountDao.decreaseAccountBalance(
                accountId = transaction.sourceId,
                delta = transaction.amount
            )
            budgetDao.increaseBudgetUsedAmount(
                category = transaction.parentCategory,
                date = transaction.date,
                delta = transaction.amount
            )
        }
    }

    override suspend fun createTransferTransaction(transaction: Transaction) {
        database.withTransaction {
            transactionDao.createNewTransaction(transaction.toEntity())
            accountDao.decreaseAccountBalance(
                accountId = transaction.sourceId,
                delta = transaction.amount
            )
            accountDao.increaseAccountBalance(
                accountId = transaction.destinationId ?: 0,
                delta = transaction.amount
            )
        }
    }

    override suspend fun getBudgetUsagePercentage(category: Int): Float {
        return budgetDao.getBudgetUsagePercentage(category)
    }

    override suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long) {
        database.withTransaction {
            transactionDao.deleteTransactionById(transactionId)
            accountDao.decreaseAccountBalance(
                accountId = sourceId,
                delta = amount
            )
        }
    }

    override suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ) {
        database.withTransaction {
            transactionDao.deleteTransactionById(transactionId)
            accountDao.increaseAccountBalance(
                accountId = sourceId,
                delta = amount
            )
            budgetDao.decreaseBudgetUsedAmount(
                category = parentCategory,
                date = date,
                delta = amount
            )
        }
    }

    override suspend fun updateIncomeTransaction(
        transaction: Transaction,
        initialAmount: Long
    ) {
        database.withTransaction {
            transactionDao.updateTransaction(transaction.toEntityForUpdate())
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.increaseAccountBalance(
                        accountId = transaction.sourceId,
                        delta = difference
                    )
                } else {
                    accountDao.decreaseAccountBalance(
                        accountId = transaction.sourceId,
                        delta = difference.absoluteValue
                    )
                }
            }
        }
    }

    override suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    ) {
        database.withTransaction {
            transactionDao.updateTransaction(transaction.toEntityForUpdate())
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(
                        accountId = transaction.sourceId,
                        delta = difference
                    )
                } else {
                    accountDao.increaseAccountBalance(
                        accountId = transaction.sourceId,
                        delta = difference.absoluteValue
                    )
                }
            }

            when(budgetStatus) {
                BudgetStatusState.NoOldBudget -> budgetDao.increaseBudgetUsedAmount(
                    category = transaction.parentCategory,
                    date = transaction.date,
                    delta = transaction.amount
                )
                BudgetStatusState.NoNewBudget -> budgetDao.decreaseBudgetUsedAmount(
                    category = initialParentCategory,
                    date = initialDate,
                    delta = initialAmount
                )
                BudgetStatusState.NoBudget -> {}
                BudgetStatusState.SameBudget -> {
                    if (difference != 0L) {
                        if (difference > 0) {
                            budgetDao.increaseBudgetUsedAmount(
                                category = transaction.parentCategory,
                                date = transaction.date,
                                delta = difference
                            )
                        } else {
                            budgetDao.decreaseBudgetUsedAmount(
                                category = transaction.parentCategory,
                                date = transaction.date,
                                delta = difference.absoluteValue
                            )
                        }
                    }
                }
                BudgetStatusState.DifferentBudget -> {
                    budgetDao.decreaseBudgetUsedAmount(
                        category = initialParentCategory,
                        date = initialDate,
                        delta = initialAmount
                    )
                    budgetDao.increaseBudgetUsedAmount(
                        category = transaction.parentCategory,
                        date = transaction.date,
                        delta = transaction.amount
                    )
                }
            }
        }
    }

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDao.getAllTransactions(
                    query = query,
                    type = type,
                    month = month,
                    year = year
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<Transaction?> {
        return transactionDao.getTransactionById(transactionId).map { transaction ->
            transaction?.toDomain()
        }
    }
}