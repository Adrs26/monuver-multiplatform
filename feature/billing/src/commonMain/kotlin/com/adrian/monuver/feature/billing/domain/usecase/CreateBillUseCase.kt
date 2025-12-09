package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.model.AddBill
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateBillUseCase(
    private val repository: BillRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(bill: AddBill): DatabaseResultState {
        when {
            bill.title.isEmpty() -> return DatabaseResultState.EmptyBillTitle
            bill.date.isEmpty() -> return DatabaseResultState.EmptyBillDate
            bill.amount == 0L -> return DatabaseResultState.EmptyBillAmount
            bill.isRecurring && bill.period == 2 && bill.fixPeriod.isEmpty() ->
                return DatabaseResultState.EmptyBillFixPeriod
        }

        val bill = Bill(
            title = bill.title,
            dueDate = bill.date,
            paidDate = null,
            amount = bill.amount,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            isRecurring = bill.isRecurring,
            cycle = if (bill.isRecurring) bill.cycle else null,
            period = if (bill.isRecurring) bill.period else null,
            fixPeriod = if (bill.isRecurring && bill.period == 2) bill.fixPeriod.toInt() else null,
            isPaid = false,
            nowPaidPeriod = 1,
            isPaidBefore = false
        )

        val billId = repository.createNewBill(bill)
        repository.updateBillParentId(
            id = billId,
            parentId = billId
        )
        return DatabaseResultState.CreateBillSuccess
    }
}