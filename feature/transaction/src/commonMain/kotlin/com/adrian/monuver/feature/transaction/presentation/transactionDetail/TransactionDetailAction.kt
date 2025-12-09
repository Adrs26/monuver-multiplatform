package com.adrian.monuver.feature.transaction.presentation.transactionDetail

import com.adrian.monuver.core.domain.model.Transaction

internal sealed interface TransactionDetailAction {
    data object NavigateBack : TransactionDetailAction
    data class  NavigateToEditTransaction(val id: Long) : TransactionDetailAction
    data class  RemoveCurrentTransaction(val transaction: Transaction) : TransactionDetailAction
}