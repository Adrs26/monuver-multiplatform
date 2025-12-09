package com.adrian.monuver.feature.billing.presentation.billDetail

internal sealed interface BillDetailAction {
    data object NavigateBack : BillDetailAction
    data class  NavigateToEditBill(val billId: Long) : BillDetailAction
    data class  RemoveBill(val billId: Long) : BillDetailAction
    data class  NavigateToPayBill(val billId: Long) : BillDetailAction
    data class  CancelBillPayment(val billId: Long) : BillDetailAction
}