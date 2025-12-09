package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.repository.CoreRepository

class GetUnpaidBillsUseCase(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(): List<Bill> {
        return repository.getAllUnpaidBills()
    }
}