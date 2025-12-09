package com.adrian.monuver.feature.billing.domain.repository

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal interface BillRepository {

    fun getPendingBills(): Flow<List<Bill>>

    fun getDueBills(): Flow<List<Bill>>

    fun getPaidBills(scope: CoroutineScope): Flow<PagingData<Bill>>

    fun getBillById(id: Long): Flow<Bill?>

    suspend fun createNewBill(bill: Bill): Long

    suspend fun updateBillParentId(id: Long, parentId: Long)

    suspend fun deleteBillById(billId: Long)

    suspend fun updateBill(bill: Bill)

    suspend fun cancelBillPayment(billId: Long)

    suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transaction: Transaction,
        isRecurring: Boolean,
        bill: Bill
    )
}