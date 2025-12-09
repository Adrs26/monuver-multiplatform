package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

internal class GetDueBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(): Flow<List<Bill>> {
        return repository.getDueBills()
    }
}