package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.saving.domain.model.DepositWithdraw
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateWithdrawTransactionUseCase(
    private val repository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(withdraw: DepositWithdraw): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = if (withdraw.date.isEmpty()) today else LocalDate.parse(withdraw.date)

        when {
            withdraw.date.isEmpty() -> return DatabaseResultState.EmptyWithdrawDate
            date > today -> return DatabaseResultState.TransactionDateAfterToday
            withdraw.amount == 0L -> return DatabaseResultState.EmptyWithdrawAmount
            withdraw.accountId == 0 -> return DatabaseResultState.EmptyWithdrawAccount
        }

        val savingBalance = repository.getSavingBalance(withdraw.savingId)
        if (savingBalance == null || savingBalance < withdraw.amount) {
            return DatabaseResultState.InsufficientSavingBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(withdraw.date)
        val transaction = Transaction(
            title = "Penarikan Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_OUT,
            date = withdraw.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = withdraw.amount,
            sourceId = withdraw.savingId.toInt(),
            sourceName = withdraw.savingName,
            destinationId = withdraw.accountId,
            destinationName = withdraw.accountName,
            saveId = withdraw.savingId,
            isLocked = true,
            isSpecialCase = true
        )

        repository.createWithdrawTransaction(
            savingId = withdraw.savingId,
            transaction = transaction
        )
        return DatabaseResultState.CreateWithdrawTransactionSuccess
    }
}