package com.adrian.monuver.feature.account.presentation.editAccount

import com.adrian.monuver.feature.account.domain.model.EditAccount

internal sealed interface EditAccountAction {
    data object NavigateBack : EditAccountAction
    data object NavigateToType : EditAccountAction
    data class  EditCurrentAccount(val account: EditAccount) : EditAccountAction
}