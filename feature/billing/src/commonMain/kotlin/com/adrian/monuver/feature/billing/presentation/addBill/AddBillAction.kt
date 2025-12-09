package com.adrian.monuver.feature.billing.presentation.addBill

import com.adrian.monuver.feature.billing.domain.model.AddBill

internal sealed interface AddBillAction {
    data object NavigateBack : AddBillAction
    data class  AddNewBill(val bill: AddBill) : AddBillAction
}