package com.adrian.monuver.feature.transaction.presentation

import com.adrian.monuver.feature.transaction.presentation.components.FilterState

internal sealed interface TransactionAction {
    data class  QueryChange(val query: String) : TransactionAction
    data object YearFilterOptionsRequest : TransactionAction
    data class  FilterApply(val filter: FilterState) : TransactionAction
    data class  NavigateToTransactionDetail(val transactionId: Long) : TransactionAction
}