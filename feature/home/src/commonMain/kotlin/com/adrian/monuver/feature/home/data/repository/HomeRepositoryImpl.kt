package com.adrian.monuver.feature.home.data.repository

import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HomeRepositoryImpl(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : HomeRepository {

    override fun getActiveAccountBalance(): Flow<Long?> {
        return accountDao.getActiveAccountBalance()
    }

    override fun getRecentTransactions(): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions().map { transactions ->
            transactions.map { it.toDomain() }
        }
    }
}