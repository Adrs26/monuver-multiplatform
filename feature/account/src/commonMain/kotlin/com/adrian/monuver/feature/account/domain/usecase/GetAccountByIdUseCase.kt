package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

internal class GetAccountByIdUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(accountId: Int): Flow<Account?> {
        return repository.getAccountById(accountId)
    }
}