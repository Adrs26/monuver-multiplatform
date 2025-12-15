package com.adrian.monuver.feature.billing.presentation.billPayment

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill

internal data class BillPaymentState(
    val bill: Bill? = null,
    val title: String = "Pembayaran Tagihan",
    val parentCategory: Int = 0,
    val childCategory: Int = 0,
    val accountId: Int = 0,
    val accountName: String = "",
    val result: DatabaseResultState = DatabaseResultState.Initial
)
