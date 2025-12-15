package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.feature.budgeting.domain.model.AddBudget
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateBudgetUseCase(
    private val coreRepository: CoreRepository,
    private val budgetRepository: BudgetRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(budget: AddBudget): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = LocalDate.parse(budget.startDate)
        val endDate = LocalDate.parse(budget.endDate)

        when {
            budget.category == 0 -> return DatabaseResultState.EmptyBudgetCategory
            budget.maxAmount == 0L -> return DatabaseResultState.EmptyBudgetMaxAmount
            startDate > today -> return DatabaseResultState.BudgetStartDateAfterToday
            endDate < today -> return DatabaseResultState.BudgetEndDateBeforeToday
        }

        if (budgetRepository.isBudgetExists(budget.category)) {
            return DatabaseResultState.ActiveBudgetWithCategoryAlreadyExists
        }

        val totalAmount = budgetRepository.getTotalTransactionAmountInDateRange(
            category = budget.category,
            startDate = budget.startDate,
            endDate = budget.endDate
        )

        if (budget.maxAmount < totalAmount && !budget.isOverflowAllowed) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        val budget = Budget(
            category = budget.category,
            cycle = budget.cycle,
            startDate = budget.startDate,
            endDate = budget.endDate,
            maxAmount = budget.maxAmount,
            usedAmount = totalAmount,
            isActive = true,
            isOverflowAllowed = budget.isOverflowAllowed,
            isAutoUpdate = if (budget.cycle == Cycle.CUSTOM) false else budget.isAutoUpdate
        )

        coreRepository.createNewBudget(budget)
        return DatabaseResultState.CreateBudgetSuccess
    }
}