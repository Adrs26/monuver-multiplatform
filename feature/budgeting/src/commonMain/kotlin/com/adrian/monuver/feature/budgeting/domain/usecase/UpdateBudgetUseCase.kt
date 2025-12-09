package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.feature.budgeting.domain.model.EditBudget
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class UpdateBudgetUseCase(
    private val repository: BudgetRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(budget: EditBudget): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = LocalDate.parse(budget.startDate)
        val endDate = LocalDate.parse(budget.endDate)

        when {
            budget.maxAmount == 0L -> return DatabaseResultState.EmptyBudgetMaxAmount
            startDate > today -> return DatabaseResultState.BudgetStartDateAfterToday
            endDate < today -> return DatabaseResultState.BudgetEndDateBeforeToday
        }

        val totalAmount = repository.getTotalTransactionAmountInDateRange(
            category = budget.category,
            startDate = budget.startDate,
            endDate = budget.endDate
        )

        if (budget.maxAmount < totalAmount && !budget.isOverflowAllowed) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        val budget = Budget(
            id = budget.id,
            category = budget.category,
            cycle = budget.cycle,
            startDate = budget.startDate,
            endDate = budget.endDate,
            maxAmount = budget.maxAmount,
            usedAmount = totalAmount,
            isActive = true,
            isOverflowAllowed = budget.isOverflowAllowed,
            isAutoUpdate = budget.isAutoUpdate
        )

        repository.updateBudget(budget)
        return DatabaseResultState.UpdateBudgetSuccess
    }
}