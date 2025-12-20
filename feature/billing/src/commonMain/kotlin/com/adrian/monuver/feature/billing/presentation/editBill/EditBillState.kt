package com.adrian.monuver.feature.billing.presentation.editBill

internal data class EditBillState(
    val id: Long = 0,
    val parentId: Long = 0,
    val title: String = "",
    val date: String = "",
    val amount: Long = 0,
    val timeStamp: Long = 0,
    val isRecurring: Boolean = false,
    val cycle: Int = 0,
    val period: Int = 0,
    val fixPeriod: String = "",
    val nowPaidPeriod: Int = 0,
    val isPaidBefore: Boolean = false
)
