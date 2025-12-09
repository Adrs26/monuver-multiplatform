package com.adrian.monuver.core.domain.model

data class Bill(
    val id: Long = 0L,
    val parentId: Long = 0L,
    val title: String,
    val dueDate: String,
    val paidDate: String?,
    val timeStamp: Long,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int?,
    val period: Int?,
    val fixPeriod: Int?,
    val isPaid: Boolean,
    val nowPaidPeriod: Int,
    val isPaidBefore: Boolean
)