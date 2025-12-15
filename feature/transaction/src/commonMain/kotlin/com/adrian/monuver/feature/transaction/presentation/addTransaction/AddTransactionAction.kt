package com.adrian.monuver.feature.transaction.presentation.addTransaction

import com.adrian.monuver.feature.transaction.domain.model.AddTransaction

internal sealed interface AddTransactionAction {
    data object NavigateBack : AddTransactionAction
    data class  NavigateToCategory(val transactionType: Int) : AddTransactionAction
    data object NavigateToSource : AddTransactionAction
    data class  ShowWarningAlert(val warning: Int, val category: Int) : AddTransactionAction
    data class  TitleChange(val title: String) : AddTransactionAction
    data class  AddNewTransaction(val transaction: AddTransaction) : AddTransactionAction
}