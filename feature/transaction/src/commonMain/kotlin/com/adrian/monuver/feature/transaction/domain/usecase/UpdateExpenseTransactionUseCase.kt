package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.feature.transaction.domain.common.BudgetStatusState
import com.adrian.monuver.feature.transaction.domain.model.EditTransaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class UpdateExpenseTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
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

        val accountBalance = coreRepository.getAccountBalance(transaction.sourceId)
        val difference = transaction.amount - transaction.initialAmount

        if (accountBalance == null || accountBalance < difference) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val oldBudget = coreRepository.getBudgetForDate(
            category = transaction.initialParentCategory,
            date = transaction.initialDate
        )
        val newBudget = coreRepository.getBudgetForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        val budgetStatus = getBudgetStatus(oldBudget, newBudget)

        newBudget?.let { budget ->
            if (!budget.isOverflowAllowed) {
                val amountToAdd = when (budgetStatus) {
                    BudgetStatusState.SameBudget -> difference
                    BudgetStatusState.DifferentBudget, BudgetStatusState.NoOldBudget -> transaction.amount
                    else -> 0L
                }

                if (amountToAdd > 0 && budget.usedAmount + amountToAdd > budget.maxAmount) {
                    return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
                }
            }
        }

        transactionRepository.updateExpenseTransaction(
            transaction = updatedTransaction,
            initialParentCategory = transaction.initialParentCategory,
            initialDate = transaction.initialDate,
            initialAmount = transaction.initialAmount,
            budgetStatus = budgetStatus
        )
        return DatabaseResultState.UpdateTransactionSuccess
    }

    private fun getBudgetStatus(oldBudgetState: Budget?, newBudgetState: Budget?): BudgetStatusState {
        return when {
            oldBudgetState == null && newBudgetState != null -> BudgetStatusState.NoOldBudget
            oldBudgetState != null && newBudgetState == null -> BudgetStatusState.NoNewBudget
            oldBudgetState == null && newBudgetState == null -> BudgetStatusState.NoBudget
            oldBudgetState?.category == newBudgetState?.category -> BudgetStatusState.SameBudget
            else -> BudgetStatusState.DifferentBudget
        }
    }
}