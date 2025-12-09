package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getAllAccounts()
    }
}