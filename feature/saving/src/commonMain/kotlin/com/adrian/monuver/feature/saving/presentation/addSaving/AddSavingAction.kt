package com.adrian.monuver.feature.saving.presentation.addSaving

import com.adrian.monuver.feature.saving.domain.model.AddSaving

internal sealed interface AddSavingAction {
    data object NavigateBack : AddSavingAction
    data class  AddNewSaving(val saving: AddSaving) : AddSavingAction
}