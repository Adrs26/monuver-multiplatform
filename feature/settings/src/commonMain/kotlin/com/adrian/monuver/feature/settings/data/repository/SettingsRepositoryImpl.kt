package com.adrian.monuver.feature.settings.data.repository

import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.BillDao
import com.adrian.monuver.core.data.database.dao.BudgetDao
import com.adrian.monuver.core.data.database.dao.SavingDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.entity.room.AccountEntity
import com.adrian.monuver.core.data.database.entity.room.BillEntity
import com.adrian.monuver.core.data.database.entity.room.BudgetEntity
import com.adrian.monuver.core.data.database.entity.room.SavingEntity
import com.adrian.monuver.core.data.database.entity.room.TransactionEntity
import com.adrian.monuver.core.data.database.util.withTransaction
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.settings.domain.model.AccountBackup
import com.adrian.monuver.feature.settings.domain.model.BillBackup
import com.adrian.monuver.feature.settings.domain.model.BudgetBackup
import com.adrian.monuver.feature.settings.domain.model.DataBackup
import com.adrian.monuver.feature.settings.domain.model.SavingBackup
import com.adrian.monuver.feature.settings.domain.model.TransactionBackup
import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository

internal class SettingsRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val savingDao: SavingDao,
    private val transactionDao: TransactionDao
) : SettingsRepository {

    override suspend fun getAllDataForBackup(): DataBackup {
        return database.withTransaction {
            DataBackup(
                accounts = accountDao.getAllAccountsSuspend().map { it.toBackup() },
                bills = billDao.getAllBills().map { it.toBackup() },
                budgets = budgetDao.getAllBudgets().map { it.toBackup() },
                savings = savingDao.getAllSavings().map { it.toBackup() },
                transactions = transactionDao.getAllTransactions().map { it.toBackup() }
            )
        }
    }

    override suspend fun deleteAllData() {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
        }
    }

    override suspend fun getTransactionsInRangeByDateAsc(
        startDate: String,
        endDate: String
    ): List<Transaction> {
        return transactionDao.getTransactionsInRangeByDateAsc(startDate, endDate)
            .map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDesc(
        startDate: String,
        endDate: String
    ): List<Transaction> {
        return transactionDao.getTransactionsInRangeByDateDesc(startDate, endDate)
            .map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateAscWithType(
        startDate: String,
        endDate: String
    ): List<Transaction> {
        return transactionDao.getTransactionsInRangeByDateAscWithType(startDate, endDate)
            .map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDescWithType(
        startDate: String,
        endDate: String
    ): List<Transaction> {
        return transactionDao.getTransactionsInRangeByDateDescWithType(startDate, endDate)
            .map { it.toDomain() }
    }

    override suspend fun getTotalIncomeTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalIncomeTransactionsInRange(startDate, endDate)
    }

    override suspend fun getTotalExpenseTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalExpenseTransactionsInRange(startDate, endDate)
    }

    override suspend fun restoreAllData(dataBackup: DataBackup) {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
            accountDao.insertAllAccounts(dataBackup.accounts.map { it.toEntity() })
            billDao.insertAllBills(dataBackup.bills.map { it.toEntity() })
            budgetDao.insertAllBudgets(dataBackup.budgets.map { it.toEntity() })
            savingDao.insertAllSavings(dataBackup.savings.map { it.toEntity() })
            transactionDao.insertAllTransactions(dataBackup.transactions.map { it.toEntity() })
        }
    }

    private fun AccountEntity.toBackup() = AccountBackup(
        id = id,
        name = name,
        type = type,
        balance = balance,
        isActive = isActive
    )

    private fun BillEntity.toBackup() = BillBackup(
        id = id,
        parentId = parentId,
        title = title,
        dueDate = dueDate,
        paidDate = paidDate,
        timeStamp = timeStamp,
        amount = amount,
        isRecurring = isRecurring,
        cycle = cycle,
        period = period,
        fixPeriod = fixPeriod,
        isPaid = isPaid,
        nowPaidPeriod = nowPaidPeriod,
        isPaidBefore = isPaidBefore
    )

    private fun BudgetEntity.toBackup() = BudgetBackup(
        id = id,
        category = category,
        cycle = cycle,
        startDate = startDate,
        endDate = endDate,
        maxAmount = maxAmount,
        usedAmount = usedAmount,
        isActive = isActive,
        isOverflowAllowed = isOverflowAllowed,
        isAutoUpdate = isAutoUpdate
    )

    private fun SavingEntity.toBackup() = SavingBackup(
        id = id,
        title = title,
        targetDate = targetDate,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        isActive = isActive
    )

    private fun TransactionEntity.toBackup() = TransactionBackup(
        id = id,
        title = title,
        type = type,
        parentCategory = parentCategory,
        childCategory = childCategory,
        date = date,
        month = month,
        year = year,
        timeStamp = timeStamp,
        amount = amount,
        sourceId = sourceId,
        sourceName = sourceName,
        destinationId = destinationId,
        destinationName = destinationName,
        saveId = saveId,
        billId = billId,
        isLocked = isLocked,
        isSpecialCase = isSpecialCase
    )

    private fun AccountBackup.toEntity() = AccountEntity(
        id = id,
        name = name,
        type = type,
        balance = balance,
        isActive = isActive
    )

    private fun BillBackup.toEntity() = BillEntity(
        id = id,
        parentId = parentId,
        title = title,
        dueDate = dueDate,
        paidDate = paidDate,
        timeStamp = timeStamp,
        amount = amount,
        isRecurring = isRecurring,
        cycle = cycle,
        period = period,
        fixPeriod = fixPeriod,
        isPaid = isPaid,
        nowPaidPeriod = nowPaidPeriod,
        isPaidBefore = isPaidBefore
    )

    private fun BudgetBackup.toEntity() = BudgetEntity(
        id = id,
        category = category,
        cycle = cycle,
        startDate = startDate,
        endDate = endDate,
        maxAmount = maxAmount,
        usedAmount = usedAmount,
        isActive = isActive,
        isOverflowAllowed = isOverflowAllowed,
        isAutoUpdate = isAutoUpdate
    )

    private fun SavingBackup.toEntity() = SavingEntity(
        id = id,
        title = title,
        targetDate = targetDate,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        isActive = isActive
    )

    private fun TransactionBackup.toEntity() = TransactionEntity(
        id = id,
        title = title,
        type = type,
        parentCategory = parentCategory,
        childCategory = childCategory,
        date = date,
        month = month,
        year = year,
        timeStamp = timeStamp,
        amount = amount,
        sourceId = sourceId,
        sourceName = sourceName,
        destinationId = destinationId,
        destinationName = destinationName,
        saveId = saveId,
        billId = billId,
        isLocked = isLocked,
        isSpecialCase = isSpecialCase
    )
}