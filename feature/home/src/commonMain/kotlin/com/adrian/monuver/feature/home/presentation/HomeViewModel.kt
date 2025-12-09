package com.adrian.monuver.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.adrian.monuver.core.presentation.util.toItem
import com.adrian.monuver.feature.home.domain.usecase.GetActiveAccountBalanceUseCase
import com.adrian.monuver.feature.home.domain.usecase.GetRecentTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class HomeViewModel(
    private val getActiveAccountBalanceUseCase: GetActiveAccountBalanceUseCase,
    private val getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init { observeUseCases() }

    private fun observeUseCases() {
        combine(
            getActiveAccountBalanceUseCase(),
            getRecentTransactionsUseCase().map { list -> list.map { it.toItem() } },
            getBudgetSummaryUseCase(),
        ) { balance, transactions, budget ->
            _state.update {
                it.copy(
                    totalBalance = balance ?: 0,
                    recentTransactions = transactions,
                    budgetSummary = budget
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }
}