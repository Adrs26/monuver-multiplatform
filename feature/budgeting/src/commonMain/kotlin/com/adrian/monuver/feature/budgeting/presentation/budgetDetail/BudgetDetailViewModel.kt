package com.adrian.monuver.feature.budgeting.presentation.budgetDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.util.toItem
import com.adrian.monuver.feature.budgeting.domain.usecase.DeleteBudgetUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetTransactionsByCategoryAndDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BudgetDetailViewModel(
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase,
    getTransactionsByCategoryAndDateRangeUseCase: GetTransactionsByCategoryAndDateRangeUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val budget = getBudgetByIdUseCase(savedStateHandle.toRoute<Budget.Edit>().id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = budget
        .filterNotNull()
        .flatMapLatest { budget ->
            getTransactionsByCategoryAndDateRangeUseCase(
                category = budget.category,
                startDate = budget.startDate,
                endDate = budget.endDate
            )
        }.map { transactions ->
            transactions.map { transactionState ->
                transactionState.toItem()
            }
        }
        .flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            deleteBudgetUseCase(budgetId)
        }
    }
}