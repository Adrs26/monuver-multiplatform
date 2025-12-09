package com.adrian.monuver.feature.saving.presentation.withdraw

import com.adrian.monuver.feature.saving.domain.model.DepositWithdraw

internal sealed interface WithdrawAction {
    data object NavigateBack : WithdrawAction
    data object NavigateToAccount : WithdrawAction
    data class  AddWithdraw(val withdraw: DepositWithdraw) : WithdrawAction
}