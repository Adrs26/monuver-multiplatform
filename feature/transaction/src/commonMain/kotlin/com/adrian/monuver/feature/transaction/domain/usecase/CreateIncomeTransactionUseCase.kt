package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.feature.transaction.domain.model.AddTransaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transaction: AddTransaction): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = if (transaction.date.isEmpty()) today else LocalDate.parse(transaction.date)

        when {
            transaction.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            transaction.childCategory == 0 -> return DatabaseResultState.EmptyTransactionCategory
            transaction.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            date > today -> return DatabaseResultState.TransactionDateAfterToday
            transaction.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
            transaction.accountId == 0 -> return DatabaseResultState.EmptyTransactionDestination
        }

        val (month, year) = DateHelper.getMonthAndYear(transaction.date)
        val transaction = Transaction(
            title = transaction.title,
            type = transaction.type,
            parentCategory = transaction.parentCategory,
            childCategory = transaction.childCategory,
            date = transaction.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = transaction.amount,
            sourceId = transaction.accountId,
            sourceName = transaction.accountName,
            isLocked = false,
            isSpecialCase = false
        )

        repository.createIncomeTransaction(transaction)
        return DatabaseResultState.CreateTransactionSuccess
    }
}