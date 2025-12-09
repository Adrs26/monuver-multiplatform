package com.adrian.monuver.feature.billing.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.BillDao
import com.adrian.monuver.core.data.database.dao.BudgetDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.util.withTransaction
import com.adrian.monuver.core.data.mapper.toDomain
import com.adrian.monuver.core.data.mapper.toEntity
import com.adrian.monuver.core.data.mapper.toEntityForUpdate
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BillRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : BillRepository {

    override fun getPendingBills(): Flow<List<Bill>> {
        return billDao.getPendingBills().map { bills ->
            bills.map { it.toDomain() }
        }
    }

    override fun getDueBills(): Flow<List<Bill>> {
        return billDao.getDueBills().map { bills ->
            bills.map { it.toDomain() }
        }
    }

    override fun getPaidBills(scope: CoroutineScope): Flow<PagingData<Bill>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                billDao.getPaidBills()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getBillById(id: Long): Flow<Bill?> {
        return billDao.getBillById(id).map { it?.toDomain() }
    }

    override suspend fun createNewBill(bill: Bill): Long {
        return billDao.createNewBill(bill.toEntity())
    }

    override suspend fun updateBillParentId(id: Long, parentId: Long) {
        billDao.updateParentId(id, parentId)
    }

    override suspend fun deleteBillById(billId: Long) {
        billDao.deleteBillById(billId)
    }

    override suspend fun updateBill(bill: Bill) {
        database.withTransaction {
            billDao.updateBill(bill.toEntityForUpdate())
            billDao.updateBillPeriodByParentId(bill.period, bill.fixPeriod, bill.parentId)
        }
    }

    override suspend fun cancelBillPayment(billId: Long) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(billId, null, false)
            val transaction = transactionDao.getTransactionIdByBillId(billId)
            transaction?.let {
                transactionDao.deleteTransactionById(transaction.id)
                accountDao.increaseAccountBalance(
                    accountId = transaction.sourceId,
                    delta = transaction.amount
                )
                budgetDao.decreaseBudgetUsedAmount(
                    category = transaction.parentCategory,
                    date = transaction.date,
                    delta = transaction.amount
                )
            }
        }
    }

    override suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transaction: Transaction,
        isRecurring: Boolean,
        bill: Bill
    ) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(
                billId = billId,
                paidDate = billPaidDate,
                isPaid = true
            )
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

            if (isRecurring) {
                billDao.createNewBill(bill.toEntity())
            }
        }
    }
}