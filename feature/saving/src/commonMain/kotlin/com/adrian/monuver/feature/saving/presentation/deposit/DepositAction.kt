package com.adrian.monuver.feature.saving.presentation.deposit

import com.adrian.monuver.feature.saving.domain.model.DepositWithdraw

internal sealed interface DepositAction {
    data object NavigateBack : DepositAction
    data object NavigateToAccount : DepositAction
    data class  AddDeposit(val deposit: DepositWithdraw) : DepositAction
}