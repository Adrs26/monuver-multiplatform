package com.adrian.monuver.feature.analytics.presentation.analyticsTransaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.presentation.navigation.Analytics
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.toItem
import com.adrian.monuver.feature.analytics.domain.usecase.GetAllTransactionsByCategoryUseCase
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class AnalyticsTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAllTransactionsByCategoryUseCase: GetAllTransactionsByCategoryUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    var state by mutableStateOf(AnalyticsTransactionState())
        private set

    init { observeUseCase() }

    private fun observeUseCase() {
        val args = savedStateHandle.toRoute<Analytics.Transaction>()

        viewModelScope.launch {
            getAllTransactionsByCategoryUseCase(
                category = args.category,
                month = args.month,
                year = args.year
            ).map { transactions ->
                transactions.map { it.toItem() }
            }.flowOn(customDispatcher.default).collect { transactions ->
                state = state.copy(
                    category = DatabaseCodeMapper.toParentCategoryTitle(args.category),
                    transactions = transactions
                )
            }
        }
    }
}