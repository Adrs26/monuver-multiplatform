package com.adrian.monuver.feature.transaction.presentation

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.feature.transaction.presentation.components.FilterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class TransactionState(
    val transactions: Flow<PagingData<TransactionItem>> = emptyFlow(),
    val query: String = "",
    val availableYears: List<Int> = emptyList(),
    val filter: FilterState = FilterState()
)
