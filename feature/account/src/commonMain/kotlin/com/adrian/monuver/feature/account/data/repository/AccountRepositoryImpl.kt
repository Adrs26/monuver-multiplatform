package com.adrian.monuver.feature.account.data.repository

import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.util.withTransaction
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.data.mapper.toEntity
import com.adrian.monuver.core.data.mapper.toEntityForUpdate
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AccountRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : AccountRepository {

    override suspend fun createAccount(
        account: Account,
        transaction: Transaction
    ) {
        database.withTransaction {
            val accountId = accountDao.createNewAccount(account.toEntity())
            val transactionWithAccountId = transaction.copy(sourceId = accountId.toInt())
            transactionDao.createNewTransaction(transactionWithAccountId.toEntity())
        }
    }

    override fun getAccountById(accountId: Int): Flow<Account?> {
        return accountDao.getAccountById(accountId).map { it?.toDomain() }
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override suspend fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        database.withTransaction {
            accountDao.updateAccountStatus(
                accountId = accountId,
                isActive = isActive
            )
            transactionDao.updateTransactionLockStatusByAccountId(
                accountId = accountId,
                isLocked = !isActive
            )
        }
    }

    override suspend fun updateAccount(account: Account) {
        database.withTransaction {
            accountDao.updateAccount(account.toEntityForUpdate())
            transactionDao.updateAccountNameOnCommonTransaction(
                accountId = account.id,
                accountName = account.name
            )
            transactionDao.updateAccountNameOnTransferTransaction(
                accountId = account.id,
                accountName = account.name
            )
            transactionDao.updateAccountNameOnDepositTransaction(
                accountId = account.id,
                accountName = account.name
            )
            transactionDao.updateAccountNameOnWithdrawTransaction(
                accountId = account.id,
                accountName = account.name
            )
        }
    }
}