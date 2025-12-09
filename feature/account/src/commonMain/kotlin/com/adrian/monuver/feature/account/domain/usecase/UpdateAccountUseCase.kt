package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.feature.account.domain.model.EditAccount
import com.adrian.monuver.feature.account.domain.repository.AccountRepository

internal class UpdateAccountUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: EditAccount): DatabaseResultState {
        when {
            account.name.isEmpty() -> return DatabaseResultState.EmptyAccountName
            account.type == 0 -> return DatabaseResultState.EmptyAccountType
        }

        val newAccount = Account(
            id = account.id,
            name = account.name,
            type = account.type,
            balance = account.balance,
            isActive = true
        )

        repository.updateAccount(newAccount)
        return DatabaseResultState.UpdateAccountSuccess
    }
}