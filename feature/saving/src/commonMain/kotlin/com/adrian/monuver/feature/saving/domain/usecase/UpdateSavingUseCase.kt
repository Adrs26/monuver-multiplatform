package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.feature.saving.domain.model.EditSaving
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository

internal class UpdateSavingUseCase(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(saving: EditSaving): DatabaseResultState {
        when {
            saving.title.isEmpty() -> return DatabaseResultState.EmptySavingTitle
            saving.targetDate.isEmpty() -> return DatabaseResultState.EmptySavingTargetDate
            saving.targetAmount == 0L -> return DatabaseResultState.EmptySavingTargetAmount
        }

        val saving = Saving(
            id = saving.id,
            title = saving.title,
            targetDate = saving.targetDate,
            targetAmount = saving.targetAmount,
            currentAmount = saving.currentAmount,
            isActive = true
        )

        repository.updateSaving(saving)
        return DatabaseResultState.UpdateSavingSuccess
    }
}