package com.adrian.monuver.feature.saving.data.repository

import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.SavingDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.util.withTransaction
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.data.mapper.toEntity
import com.adrian.monuver.core.data.mapper.toEntityForUpdate
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SavingRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val savingDao: SavingDao,
    private val transactionDao: TransactionDao
) : SavingRepository {

    override fun getAllActiveSavings(): Flow<List<Saving>> {
        return savingDao.getAllActiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getAllInactiveSavings(): Flow<List<Saving>> {
        return savingDao.getAllInactiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getTotalSavingCurrentAmount(): Flow<Long?> {
        return savingDao.getTotalSavingCurrentAmount()
    }

    override fun getSavingById(savingId: Long): Flow<Saving?> {
        return savingDao.getSavingById(savingId).map { it?.toDomain() }
    }

    override suspend fun getSavingBalance(savingId: Long): Long? {
        return savingDao.getSavingBalance(savingId)
    }

    override suspend fun createNewSaving(saving: Saving) {
        savingDao.createNewSaving(saving.toEntity())
    }

    override suspend fun deleteSavingById(savingId: Long) {
        savingDao.deleteSavingById(savingId)
    }

    override suspend fun createDepositTransaction(
        savingId: Long,
        transaction: Transaction
    ) {
        return database.withTransaction {
            savingDao.increaseSavingCurrentAmount(
                savingId = savingId,
                delta = transaction.amount
            )
            accountDao.decreaseAccountBalance(
                accountId = transaction.sourceId,
                delta = transaction.amount
            )
            transactionDao.createNewTransaction(transaction.toEntity())
        }
    }

    override suspend fun createWithdrawTransaction(
        savingId: Long,
        transaction: Transaction
    ) {
        return database.withTransaction {
            savingDao.decreaseSavingCurrentAmount(
                savingId = savingId,
                delta = transaction.amount
            )
            accountDao.increaseAccountBalance(
                accountId = transaction.destinationId ?: 0,
                delta = transaction.amount
            )
            transactionDao.createNewTransaction(transaction.toEntity())
        }
    }

    override suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    ) {
        return database.withTransaction {
            if (category == TransactionChildCategory.SAVINGS_IN) {
                savingDao.decreaseSavingCurrentAmount(
                    savingId = savingId,
                    delta = amount
                )
                accountDao.increaseAccountBalance(
                    accountId = accountId,
                    delta = amount
                )
            } else {
                savingDao.increaseSavingCurrentAmount(
                    savingId = savingId,
                    delta = amount
                )
                accountDao.decreaseAccountBalance(
                    accountId = accountId,
                    delta = amount
                )
            }
            transactionDao.deleteTransactionById(transactionId)
        }
    }

    override suspend fun updateSaving(saving: Saving) {
        database.withTransaction {
            savingDao.updateSaving(saving.toEntityForUpdate())
            transactionDao.updateSavingTitleOnDepositTransaction(
                savingId = saving.id,
                savingTitle = saving.title
            )
            transactionDao.updateSavingTitleOnWithdrawTransaction(
                savingId = saving.id,
                savingTitle = saving.title
            )
        }
    }

    override suspend fun completeSaving(transaction: Transaction, savingId: Long) {
        database.withTransaction {
            transactionDao.createNewTransaction(transaction.toEntity())
            savingDao.updateSavingStatusToInactiveById(savingId)
        }
    }

    override fun getTransactionsBySavingId(savingId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsBySavingId(savingId).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<Transaction> {
        return transactionDao.getTransactionsBySavingIdSuspend(savingId).map { it.toDomain() }
    }
}