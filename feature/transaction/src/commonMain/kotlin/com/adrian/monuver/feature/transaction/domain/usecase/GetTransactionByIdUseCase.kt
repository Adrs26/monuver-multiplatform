package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

internal class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Long): Flow<Transaction?> {
        return repository.getTransactionById(id)
    }
}