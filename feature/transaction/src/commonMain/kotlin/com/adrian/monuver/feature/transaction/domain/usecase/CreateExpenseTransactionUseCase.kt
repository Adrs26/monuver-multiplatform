package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.BudgetWarningCondition
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.feature.transaction.domain.model.AddTransaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateExpenseTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
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
            transaction.accountId == 0 -> return DatabaseResultState.EmptyTransactionSource
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

        val accountBalance = coreRepository.getAccountBalance(transaction.sourceId)
        val budget = coreRepository.getBudgetForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        if (accountBalance == null || accountBalance < transaction.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        if (
            budget != null &&
            budget.usedAmount + transaction.amount > budget.maxAmount &&
            !budget.isOverflowAllowed
        ) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        transactionRepository.createExpenseTransaction(transaction)

        if (budget != null) {
            val budgetUsage = transactionRepository.getBudgetUsagePercentage(budget.category)
            return calculateBudgetUsage(budgetUsage, budget.category)
        }

        return DatabaseResultState.CreateTransactionSuccess
    }

    private fun calculateBudgetUsage(budgetUsage: Float, budgetCategory: Int): DatabaseResultState {
        return when {
            budgetUsage > 100F -> {
                DatabaseResultState.CreateSuccessWithWarningCondition(
                    category = budgetCategory,
                    warningCondition = BudgetWarningCondition.OVER_BUDGET
                )
            }
            budgetUsage >= 100F -> DatabaseResultState.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.FULL_BUDGET
            )
            budgetUsage >= 80F -> DatabaseResultState.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.LOW_REMAINING_BUDGET
            )
            else -> DatabaseResultState.CreateTransactionSuccess
        }
    }
}