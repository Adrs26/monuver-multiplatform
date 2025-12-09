package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.model.BudgetSummary
import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBudgetSummaryUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<BudgetSummary> {
        return combine(
            repository.getTotalBudgetMaxAmount(),
            repository.getTotalBudgetUsedAmount()
        ) { maxAmount, usedAmount ->
            BudgetSummary(
                totalMaxAmount = maxAmount,
                totalUsedAmount = usedAmount
            )
        }
    }
}