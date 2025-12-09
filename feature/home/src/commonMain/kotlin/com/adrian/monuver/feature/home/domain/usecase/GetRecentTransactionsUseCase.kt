package com.adrian.monuver.feature.home.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

internal class GetRecentTransactionsUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getRecentTransactions()
    }
}