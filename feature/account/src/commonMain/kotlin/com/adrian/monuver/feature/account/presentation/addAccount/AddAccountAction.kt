package com.adrian.monuver.feature.account.presentation.addAccount

import com.adrian.monuver.feature.account.domain.model.AddAccount

internal sealed interface AddAccountAction {
    data object NavigateBack : AddAccountAction
    data object NavigateToType : AddAccountAction
    data class  AddNewAccount(val account: AddAccount) : AddAccountAction
}