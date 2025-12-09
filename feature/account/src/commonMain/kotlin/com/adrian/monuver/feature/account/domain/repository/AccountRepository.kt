package com.adrian.monuver.feature.account.domain.repository

import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

internal interface AccountRepository {

    suspend fun createAccount(account: Account, transaction: Transaction)

    fun getAccountById(accountId: Int): Flow<Account?>

    fun getAllAccounts(): Flow<List<Account>>

    fun getTotalAccountBalance(): Flow<Long?>

    suspend fun updateAccountStatus(accountId: Int, isActive: Boolean)

    suspend fun updateAccount(account: Account)
}