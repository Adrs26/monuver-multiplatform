package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BillBackup(
    @SerialName("id") val id: Long = 0L,
    @SerialName("parent_id") val parentId: Long = 0L,
    @SerialName("title") val title: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("paid_date") val paidDate: String? = null,
    @SerialName("time_stamp") val timeStamp: Long,
    @SerialName("amount") val amount: Long,
    @SerialName("is_recurring") val isRecurring: Boolean,
    @SerialName("cycle") val cycle: Int? = null,
    @SerialName("period") val period: Int? = null,
    @SerialName("fix_period") val fixPeriod: Int? = null,
    @SerialName("is_paid") val isPaid: Boolean,
    @SerialName("now_paid_period") val nowPaidPeriod: Int,
    @SerialName("is_paid_before") val isPaidBefore: Boolean
)
