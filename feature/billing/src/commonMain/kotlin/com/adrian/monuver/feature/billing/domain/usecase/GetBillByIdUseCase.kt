package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

internal class GetBillByIdUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(id: Long): Flow<Bill?> {
        return repository.getBillById(id)
    }
}