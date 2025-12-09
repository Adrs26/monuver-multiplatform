package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

internal class GetTotalAccountBalanceUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getTotalAccountBalance()
    }
}