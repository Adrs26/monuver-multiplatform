package com.adrian.monuver.feature.home.presentation

internal sealed interface HomeAction {
    data object NavigateToBill : HomeAction
    data object NavigateToSaving : HomeAction
    data object NavigateToSettings : HomeAction
    data object NavigateToAccount : HomeAction
    data object NavigateToAddIncome : HomeAction
    data object NavigateToAddExpense : HomeAction
    data object NavigateToTransfer : HomeAction
    data object NavigateToTransaction : HomeAction
    data class  NavigateToTransactionDetail(val transactionId: Long) : HomeAction
    data object NavigateToAddBudget : HomeAction
    data object NavigateToBudgeting : HomeAction
}