package com.adrian.monuver.feature.transaction.presentation.editTransaction

import com.adrian.monuver.feature.transaction.domain.model.EditTransaction

internal sealed interface EditTransactionAction {
    data object NavigateBack : EditTransactionAction
    data class  NavigateToCategory(val type: Int) : EditTransactionAction
    data class  EditCurrentTransaction(val transaction: EditTransaction) : EditTransactionAction
}