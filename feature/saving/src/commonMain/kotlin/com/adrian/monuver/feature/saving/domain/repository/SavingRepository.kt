package com.adrian.monuver.feature.saving.domain.repository

import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

internal interface SavingRepository {

    fun getAllActiveSavings(): Flow<List<Saving>>

    fun getAllInactiveSavings(): Flow<List<Saving>>

    fun getTotalSavingCurrentAmount(): Flow<Long?>

    fun getSavingById(savingId: Long): Flow<Saving?>

    suspend fun getSavingBalance(savingId: Long): Long?

    suspend fun createNewSaving(saving: Saving)

    suspend fun deleteSavingById(savingId: Long)

    suspend fun createDepositTransaction(savingId: Long, transaction: Transaction)

    suspend fun createWithdrawTransaction(savingId: Long, transaction: Transaction)

    suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    )

    suspend fun updateSaving(saving: Saving)

    suspend fun completeSaving(transaction: Transaction, savingId: Long)

    fun getTransactionsBySavingId(savingId: Long): Flow<List<Transaction>>

    suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<Transaction>
}