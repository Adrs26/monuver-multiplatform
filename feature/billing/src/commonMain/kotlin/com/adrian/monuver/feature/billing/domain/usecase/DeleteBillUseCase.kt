package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.feature.billing.domain.repository.BillRepository

internal class DeleteBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBillById(id)
    }
}