package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetActiveAccountsUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getActiveAccounts()
    }
}