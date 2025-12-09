package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.feature.transaction.domain.model.EditTransaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class UpdateIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transaction: EditTransaction): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = LocalDate.parse(transaction.date)

        when {
            transaction.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            date > today -> return DatabaseResultState.TransactionDateAfterToday
            transaction.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transaction.date)
        val updatedTransaction = Transaction(
            id = transaction.id,
            title = transaction.title,
            type = transaction.type,
            parentCategory = transaction.parentCategory,
            childCategory = transaction.childCategory,
            date = transaction.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = transaction.amount,
            sourceId = transaction.sourceId,
            sourceName = transaction.sourceName,
            isLocked = transaction.isLocked,
            isSpecialCase = false
        )

        repository.updateIncomeTransaction(
            transaction = updatedTransaction,
            initialAmount = transaction.initialAmount
        )
        return DatabaseResultState.UpdateTransactionSuccess
    }
}