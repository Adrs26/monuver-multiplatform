package com.adrian.monuver.feature.billing.domain.model

internal data class EditBill(
    val id: Long,
    val parentId: Long,
    val title: String,
    val date: String,
    val amount: Long,
    val timeStamp: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String,
    val nowPaidPeriod: Int,
    val isPaidBefore: Boolean
)