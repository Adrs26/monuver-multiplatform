package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.transaction.domain.model.Transfer
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateTransferTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transfer: Transfer): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = if (transfer.date.isEmpty()) today else LocalDate.parse(transfer.date)

        when {
            transfer.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
            transfer.destinationId == 0 -> return DatabaseResultState.EmptyTransactionDestination
            transfer.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            date > today -> return DatabaseResultState.TransactionDateAfterToday
            transfer.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transfer.date)
        val transaction = Transaction(
            title = "Transfer Saldo",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.TRANSFER_ACCOUNT,
            date = transfer.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = transfer.amount,
            sourceId = transfer.sourceId,
            sourceName = transfer.sourceName,
            destinationId = transfer.destinationId,
            destinationName = transfer.destinationName,
            isLocked = true,
            isSpecialCase = true
        )

        val accountBalance = coreRepository.getAccountBalance(transaction.sourceId)

        if (accountBalance == null || accountBalance < transaction.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        transactionRepository.createTransferTransaction(transaction)
        return DatabaseResultState.CreateTransactionSuccess
    }
}