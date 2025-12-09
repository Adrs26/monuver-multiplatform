package com.adrian.monuver.feature.account.presentation.accountDetail

internal sealed interface AccountDetailAction {
    data object NavigateBack : AccountDetailAction
    data class  NavigateToEditAccount(val accountId: Int) : AccountDetailAction
    data class  DeactivateAccount(val accountId: Int) : AccountDetailAction
    data class  ActivateAccount(val accountId: Int) : AccountDetailAction
}