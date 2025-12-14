package com.adrian.monuver.feature.analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionBalanceSummaryUseCase
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionCategorySummaryUseCase
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionSummaryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class AnalyticsViewModel(
    private val preferences: MonuverPreferences,
    private val getTransactionBalanceSummaryUseCase: GetTransactionBalanceSummaryUseCase,
    private val getTransactionCategorySummaryUseCase: GetTransactionCategorySummaryUseCase,
    private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state = _state.asStateFlow()

    init {
        observeThemeState()
        observeAvailableYears()
        observeBalanceSummary()
        observeDailySummaries()
        observeCategorySummaries()
    }

    private fun observeThemeState() {
        preferences.themeState.onEach { themeState ->
            _state.update {
                it.copy(themeState = themeState)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeAvailableYears() {
        getDistinctTransactionYearsUseCase().onEach { availableYears ->
            _state.update {
                it.copy(availableYears = availableYears)
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeBalanceSummary() {
        _state.map { it.monthFilter to it.yearFilter }
            .flatMapLatest { (month, year) ->
                getTransactionBalanceSummaryUseCase(
                    month = month,
                    year = year
                )
            }.onEach { summary ->
                _state.update {
                    it.copy(balanceSummary = summary)
                }
            }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeDailySummaries() {
        _state.map { Triple(it.monthFilter, it.yearFilter, it.weekFilter) }
            .flatMapLatest { (month, year, week) ->
                getTransactionSummaryUseCase(
                    month = month,
                    year = year,
                    week = week
                )
            }.onEach { summaries ->
                _state.update {
                    it.copy(dailySummaries = summaries)
                }
            }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCategorySummaries() {
        _state.map { Triple(it.monthFilter, it.yearFilter, it.typeFilter) }
            .flatMapLatest { (month, year, type) ->
                getTransactionCategorySummaryUseCase(
                    month = month,
                    year = year,
                    type = type
                )
            }.onEach { summaries ->
                _state.update {
                    it.copy(categorySummaries = summaries)
                }
            }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun setMonthFilter(month: Int) {
        _state.update {
            it.copy(monthFilter = month)
        }
        changeWeekToAvailableWeek()
    }

    fun setYearFilter(year: Int) {
        _state.update {
            it.copy(yearFilter = year)
        }
        changeWeekToAvailableWeek()
    }

    fun setWeekFilter(week: Int) {
        _state.update {
            it.copy(weekFilter = week)
        }
    }

    fun setTypeFilter(type: Int) {
        _state.update {
            it.copy(typeFilter = type)
        }
    }

    private fun changeWeekToAvailableWeek() {
        val isAvailableWeeks = DateHelper.getWeekOptions(
            month = _state.value.monthFilter,
            year = _state.value.yearFilter
        ).contains(_state.value.weekFilter)

        if (!isAvailableWeeks) {
            _state.update { it.copy(weekFilter = 4) }
        }
    }
}