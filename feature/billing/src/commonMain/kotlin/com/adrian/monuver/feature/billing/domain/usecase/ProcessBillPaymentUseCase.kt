package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.billing.domain.model.BillPayment
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ProcessBillPaymentUseCase(
    private val coreRepository: CoreRepository,
    private val billRepository: BillRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(bill: Bill, billPayment: BillPayment): DatabaseResultState {
        when {
            billPayment.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            billPayment.childCategory == 0 -> return DatabaseResultState.EmptyTransactionCategory
            billPayment.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
            billPayment.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            billPayment.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
        }

        val accountBalance = coreRepository.getAccountBalance(billPayment.sourceId)
        if (accountBalance == null || accountBalance < billPayment.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val budget = coreRepository.getBudgetForDate(
            category = billPayment.parentCategory,
            date = billPayment.date
        )
        if (
            budget != null &&
            budget.usedAmount + billPayment.amount > budget.maxAmount &&
            !budget.isOverflowAllowed
        ) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        val (month, year) = DateHelper.getMonthAndYear(billPayment.date)
        val transaction = Transaction(
            title = billPayment.title,
            type = TransactionType.EXPENSE,
            parentCategory = billPayment.parentCategory,
            childCategory = billPayment.childCategory,
            date = billPayment.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = billPayment.amount,
            sourceId = billPayment.sourceId,
            sourceName = billPayment.sourceName,
            billId = bill.id,
            isLocked = true,
            isSpecialCase = true
        )

        val isRecurring = !bill.isPaidBefore && ((bill.period == 1) ||
                (bill.nowPaidPeriod < (bill.fixPeriod ?: 0)))

        val newBill = Bill(
            parentId = bill.parentId,
            title = bill.title,
            dueDate = getNewDueDate(bill.cycle, bill.dueDate),
            paidDate = null,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = bill.amount,
            isRecurring = bill.isRecurring,
            cycle = bill.cycle,
            period = bill.period,
            fixPeriod = bill.fixPeriod,
            isPaid = false,
            nowPaidPeriod = bill.nowPaidPeriod + 1,
            isPaidBefore = false
        )

        billRepository.processBillPayment(
            billId = bill.id,
            billPaidDate = billPayment.date,
            transaction = transaction,
            isRecurring = isRecurring,
            bill = newBill
        )
        return DatabaseResultState.PayBillSuccess
    }

    private fun getNewDueDate(cycle: Int?, dueDate: String): String {
        val date = LocalDate.parse(dueDate)

        return when (cycle) {
            Cycle.YEARLY -> date.plus(1, DateTimeUnit.YEAR).toString()
            Cycle.MONTHLY -> date.plus(1, DateTimeUnit.MONTH).toString()
            Cycle.WEEKLY -> date.plus(7, DateTimeUnit.DAY).toString()
            else -> return ""
        }
    }
}