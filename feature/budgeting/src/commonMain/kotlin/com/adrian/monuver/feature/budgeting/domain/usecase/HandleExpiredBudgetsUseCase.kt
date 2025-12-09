package com.adrian.monuver.feature.budgeting.domain.usecase

import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.util.Cycle
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class HandleExpiredBudgetsUseCase(
    private val repository: CoreRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val budgets = repository.getAllActiveBudgets().first()
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        budgets.forEach { budget ->
            if (isBudgetExpired(budget.endDate, today)) {
                repository.updateBudgetStatusToInactive(budget.category)

                if (budget.isAutoUpdate) {
                    var newStartDate = LocalDate.parse(budget.startDate)
                    var newEndDate = LocalDate.parse(budget.endDate)

                    while (newEndDate < today) {
                        newStartDate = shiftDate(budget.cycle, newStartDate, true)
                        newEndDate = shiftDate(budget.cycle, newEndDate, false)
                    }

                    if (newEndDate >= today) {
                        val newBudget = Budget(
                            category = budget.category,
                            cycle = budget.cycle,
                            startDate = newStartDate.toString(),
                            endDate = newEndDate.toString(),
                            maxAmount = budget.maxAmount,
                            usedAmount = 0L,
                            isActive = true,
                            isOverflowAllowed = budget.isOverflowAllowed,
                            isAutoUpdate = true,
                        )
                        repository.createNewBudget(newBudget)
                    }
                }
            }
        }
    }

    private fun isBudgetExpired(
        endDate: String,
        today: LocalDate
    ): Boolean {
        val end = LocalDate.parse(endDate)
        return end < today
    }

    private fun shiftDate(period: Int, date: LocalDate, isStartDate: Boolean): LocalDate {
        return when (period) {
            Cycle.MONTHLY -> {
                val shifted = date.plus(DatePeriod(months = 1))

                if (isStartDate) {
                    shifted
                } else {
                    val lastDay = daysInMonth(shifted.month.number, shifted.year)
                    LocalDate(shifted.year, shifted.month.number, lastDay)
                }
            }

            Cycle.WEEKLY -> date.plus(DatePeriod(days = 7))

            else -> date
        }
    }

    private fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}