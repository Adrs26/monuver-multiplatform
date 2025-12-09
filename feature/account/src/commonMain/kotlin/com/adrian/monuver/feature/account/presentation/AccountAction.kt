package com.adrian.monuver.feature.account.presentation

internal sealed interface AccountAction {
    data object NavigateBack : AccountAction
    data object NavigateToAddAccount : AccountAction
    data class  NavigateToAccountDetail(val accountId: Int) : AccountAction
}