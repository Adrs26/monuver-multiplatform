package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.account.domain.repository.AccountRepository


internal class UpdateAccountStatusUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(accountId: Int, isActive: Boolean): DatabaseResultState {
        repository.updateAccountStatus(
            accountId = accountId,
            isActive = isActive
        )
        return if (isActive) {
            DatabaseResultState.ActivateAccountSuccess
        } else {
            DatabaseResultState.DeactivateAccountSuccess
        }
    }
}