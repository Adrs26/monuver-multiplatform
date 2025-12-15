package com.adrian.monuver.feature.billing.presentation.billPayment

import com.adrian.monuver.feature.billing.domain.model.BillPayment

internal sealed interface BillPaymentAction {
    data object NavigateBack : BillPaymentAction
    data object NavigateToCategory : BillPaymentAction
    data object NavigateToSource : BillPaymentAction
    data class  TitleChange(val title: String) : BillPaymentAction
    data class  PayCurrentBill(val bill: BillPayment) : BillPaymentAction
}