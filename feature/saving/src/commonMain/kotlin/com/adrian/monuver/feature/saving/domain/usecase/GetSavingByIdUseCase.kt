package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetSavingByIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(saveId: Long): Flow<Saving?> {
        return repository.getSavingById(saveId)
    }
}