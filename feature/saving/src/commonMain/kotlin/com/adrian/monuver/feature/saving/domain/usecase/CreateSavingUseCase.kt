package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.feature.saving.domain.model.AddSaving
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateSavingUseCase(
    private val repository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(saving: AddSaving): DatabaseResultState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = if (saving.targetDate.isEmpty()) today else LocalDate.parse(saving.targetDate)

        when {
            saving.title.isEmpty() -> return DatabaseResultState.EmptySavingTitle
            saving.targetDate.isEmpty() -> return DatabaseResultState.EmptySavingTargetDate
            date < today -> return DatabaseResultState.SavingTargetDateBeforeToday
            saving.targetAmount == 0L -> return DatabaseResultState.EmptySavingTargetAmount
        }

        val saving = Saving(
            title = saving.title,
            targetDate = saving.targetDate,
            targetAmount = saving.targetAmount,
            currentAmount = 0,
            isActive = true
        )

        repository.createNewSaving(saving)
        return DatabaseResultState.CreateSavingSuccess
    }
}