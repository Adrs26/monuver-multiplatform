package com.adrian.monuver.feature.saving.presentation.savingDetail

internal sealed interface SavingDetailAction {
    data object NavigateBack : SavingDetailAction
    data class  NavigateToEditSaving(val savingId: Long) : SavingDetailAction
    data class  RemoveSaving(val savingId: Long) : SavingDetailAction
    data object NavigateToDeposit : SavingDetailAction
    data object NavigateToWithdraw : SavingDetailAction
    data class  NavigateToTransactionDetail(val transactionId: Long) : SavingDetailAction
    data class  CompleteSaving(val savingId: Long, val savingName: String, val savingAmount: Long) : SavingDetailAction
}