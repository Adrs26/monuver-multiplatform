package com.adrian.monuver.feature.saving.presentation.editSaving

import com.adrian.monuver.feature.saving.domain.model.EditSaving

internal sealed interface EditSavingAction {
    data object NavigateBack : EditSavingAction
    data class  EditCurrentSaving(val saving: EditSaving) : EditSavingAction
}