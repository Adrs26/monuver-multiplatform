package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionBackup(
    @SerialName("id") val id: Long = 0,
    @SerialName("title") val title: String,
    @SerialName("type") val type: Int,
    @SerialName("parent_category") val parentCategory: Int,
    @SerialName("child_category") val childCategory: Int,
    @SerialName("date") val date: String,
    @SerialName("month") val month: Int,
    @SerialName("year") val year: Int,
    @SerialName("time_stamp") val timeStamp: Long,
    @SerialName("amount") val amount: Long,
    @SerialName("source_id") val sourceId: Int,
    @SerialName("source_name") val sourceName: String,
    @SerialName("destination_id") val destinationId: Int? = null,
    @SerialName("destination_name") val destinationName: String? = null,
    @SerialName("save_id") val saveId: Long? = null,
    @SerialName("bill_id") val billId: Long? = null,
    @SerialName("is_locked") val isLocked: Boolean,
    @SerialName("is_special_case") val isSpecialCase: Boolean
)
