package com.adrian.monuver.feature.transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.adrian.monuver.core.presentation.util.toItem
import com.adrian.monuver.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.adrian.monuver.feature.transaction.presentation.components.FilterState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TransactionViewModel(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state
        .onStart {
            observeStateChanges()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionState()
        )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeStateChanges() {
        _state.map { it.query to it.filter }
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { (query, filter) ->
                getAllTransactionsUseCase(
                    query = query,
                    type = filter.type,
                    month = filter.month,
                    year = filter.year,
                    scope = viewModelScope
                ).map { paging ->
                    paging.map {
                        it.toItem()
                    }
                }.flowOn(customDispatcher.default)
            }.onEach { pagingData ->
                _state.update {
                    it.copy(transactions = flowOf(pagingData))
                }
            }.launchIn(viewModelScope)
    }

    fun queryChange(query: String) {
        _state.update {
            it.copy(query = query)
        }
    }

    fun getYearFilterOptions() {
        viewModelScope.launch {
            getDistinctTransactionYearsUseCase().collect { availableYears ->
                _state.update { it.copy(availableYears = availableYears) }
            }
        }
    }

    fun filterApply(filter: FilterState) {
        _state.update {
            it.copy(filter = filter)
        }
    }
}