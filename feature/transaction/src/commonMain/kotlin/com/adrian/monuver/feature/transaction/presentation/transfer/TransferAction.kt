package com.adrian.monuver.feature.transaction.presentation.transfer

import com.adrian.monuver.feature.transaction.domain.model.Transfer

internal sealed interface TransferAction {
    data object NavigateBack : TransferAction
    data object NavigateToSourceAccount : TransferAction
    data object NavigateToDestinationAccount : TransferAction
    data class  AddNewTransfer(val transfer: Transfer) : TransferAction
}