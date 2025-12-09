package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.repository.CoreRepository
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

internal class CreateDepositTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val savingRepository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(deposit: DepositWithdraw): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = if (deposit.date.isEmpty()) today else LocalDate.parse(deposit.date)

        when {
            deposit.date.isEmpty() -> return DatabaseResultState.EmptyDepositDate
            date > today -> return DatabaseResultState.TransactionDateAfterToday
            deposit.amount == 0L -> return DatabaseResultState.EmptyDepositAmount
            deposit.accountId == 0 -> return DatabaseResultState.EmptyDepositAccount
        }

        val accountBalance = coreRepository.getAccountBalance(deposit.accountId)
        if (accountBalance == null || accountBalance < deposit.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(deposit.date)
        val transaction = Transaction(
            title = "Setoran Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_IN,
            date = deposit.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = deposit.amount,
            sourceId = deposit.accountId,
            sourceName = deposit.accountName,
            destinationId = deposit.savingId.toInt(),
            destinationName = deposit.savingName,
            saveId = deposit.savingId,
            isLocked = true,
            isSpecialCase = true
        )

        savingRepository.createDepositTransaction(
            savingId = deposit.savingId,
            transaction = transaction
        )
        return DatabaseResultState.CreateDepositTransactionSuccess
    }
}