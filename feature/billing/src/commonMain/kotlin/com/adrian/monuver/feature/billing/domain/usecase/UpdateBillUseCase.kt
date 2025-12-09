package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.model.EditBill
import com.adrian.monuver.feature.billing.domain.repository.BillRepository

internal class UpdateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(bill: EditBill): DatabaseResultState {
        when {
            bill.title.isEmpty() -> return DatabaseResultState.EmptyBillTitle
            bill.date.isEmpty() -> return DatabaseResultState.EmptyBillDate
            bill.amount == 0L -> return DatabaseResultState.EmptyBillAmount
            bill.isRecurring && bill.period == 2 && bill.fixPeriod.isEmpty() ->
                return DatabaseResultState.EmptyBillFixPeriod
            bill.isRecurring && bill.period == 2 && bill.fixPeriod.toInt() < bill.nowPaidPeriod ->
                return DatabaseResultState.InvalidBillFixPeriod
        }

        val bill = Bill(
            id = bill.id,
            parentId = bill.parentId,
            title = bill.title,
            dueDate = bill.date,
            paidDate = null,
            amount = bill.amount,
            timeStamp = bill.timeStamp,
            isRecurring = bill.isRecurring,
            cycle = if (bill.isRecurring) bill.cycle else null,
            period = if (bill.isRecurring) bill.period else null,
            fixPeriod = if (bill.isRecurring && bill.period == 2) bill.fixPeriod.toInt() else null,
            isPaid = false,
            nowPaidPeriod = bill.nowPaidPeriod,
            isPaidBefore = bill.isPaidBefore
        )

        repository.updateBill(bill)
        return DatabaseResultState.UpdateBillSuccess
    }
}