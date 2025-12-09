package com.adrian.monuver.feature.home.domain.usecase

import com.adrian.monuver.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

internal class GetActiveAccountBalanceUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getActiveAccountBalance()
    }
}