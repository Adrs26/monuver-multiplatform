package com.adrian.monuver.feature.billing.presentation.editBill

import com.adrian.monuver.feature.billing.domain.model.EditBill

internal sealed interface EditBillAction {
    data object NavigateBack : EditBillAction
    data class  EditCurrentBill(val bill: EditBill) : EditBillAction
}