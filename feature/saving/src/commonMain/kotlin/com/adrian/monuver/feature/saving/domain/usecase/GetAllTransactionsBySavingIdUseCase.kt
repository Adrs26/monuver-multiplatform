package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllTransactionsBySavingIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(savingId: Long): Flow<List<Transaction>> {
        return repository.getTransactionsBySavingId(savingId)
    }
}