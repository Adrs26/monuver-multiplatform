package com.adrian.monuver.feature.billing.domain.model

internal data class AddBill(
    val title: String,
    val date: String,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String
)