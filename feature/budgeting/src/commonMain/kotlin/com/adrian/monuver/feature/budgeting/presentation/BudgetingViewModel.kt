package com.adrian.monuver.feature.budgeting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem
import com.adrian.monuver.feature.budgeting.domain.usecase.GetAllActiveBudgetsUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.HandleExpiredBudgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BudgetingViewModel(
    private val handleExpiredBudgetsUseCase: HandleExpiredBudgetsUseCase,
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetingState())
    val state = _state.asStateFlow()

    init {
        observeUseCases()
        handleExpiredBudgets()
    }

    private fun observeUseCases() {
        combine(
            getBudgetSummaryUseCase(),
            getAllActiveBudgetsUseCase()
        ) { summary, budgets ->
            Pair(summary, budgets)
        }.onEach { (summary, budgets) ->
            _state.update {
                it.copy(
                    totalMaxAmount = summary.totalMaxAmount,
                    totalUsedAmount = summary.totalUsedAmount,
                    budgets = budgets.map { budget ->
                        BudgetItem(
                            id = budget.id,
                            category = budget.category,
                            startDate = budget.startDate,
                            endDate = budget.endDate,
                            maxAmount = budget.maxAmount,
                            usedAmount = budget.usedAmount
                        )
                    }
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    private fun handleExpiredBudgets() {
        viewModelScope.launch {
            handleExpiredBudgetsUseCase()
        }
    }
}