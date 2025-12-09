package com.adrian.monuver.feature.saving.presentation

internal sealed interface SavingAction {
    data object NavigateBack : SavingAction
    data object NavigateToInactiveSaving : SavingAction
    data object NavigateToAddSaving : SavingAction
    data class  NavigateToSavingDetail(val savingId: Long) : SavingAction
}