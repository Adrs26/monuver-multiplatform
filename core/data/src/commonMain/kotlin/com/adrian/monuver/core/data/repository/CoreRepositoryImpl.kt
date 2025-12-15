package com.adrian.monuver.core.data.repository

import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.BillDao
import com.adrian.monuver.core.data.database.dao.BudgetDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.data.mapper.toEntity
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation
import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoreRepositoryImpl(
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : CoreRepository {

    override fun getActiveAccounts(): Flow<List<Account>> {
        return accountDao.getActiveAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override suspend fun getAccountBalance(accountId: Int): Long? {
        return accountDao.getAccountBalance(accountId)
    }

    override fun getDistinctTransactionYears(): Flow<List<Int>> {
        return transactionDao.getDistinctTransactionYears()
    }

    override fun getTotalBudgetMaxAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetMaxAmount()
    }

    override fun getTotalBudgetUsedAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetUsedAmount()
    }

    override fun getAllActiveBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllActiveBudgets().map { budgets ->
            budgets.map { it.toDomain() }
        }
    }

    override fun getCategoryRecommendationByTitle(
        title: String,
        type: Int
    ): Flow<TransactionCategoryRecommendation> {
        return transactionDao.getCategoryRecommendationByTitle(
            title = title,
            type = type
        ).map { it?.toDomain() ?: TransactionCategoryRecommendation(0, 0) }
    }

    override suspend fun createNewBudget(budget: Budget) {
        budgetDao.createNewBudget(budget.toEntity())
    }

    override suspend fun updateBudgetStatusToInactive(category: Int) {
        budgetDao.updateBudgetStatusToInactive(category)
    }

    override suspend fun getBudgetForDate(
        category: Int,
        date: String
    ): Budget? {
        return budgetDao.getBudgetForDate(category, date)?.toDomain()
    }

    override suspend fun getAllUnpaidBills(): List<Bill> {
        return billDao.getAllUnpaidBills().map { it.toDomain() }
    }
}