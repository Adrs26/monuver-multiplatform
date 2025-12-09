package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetDistinctTransactionYearsUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<List<Int>> {
        return repository.getDistinctTransactionYears()
    }
}