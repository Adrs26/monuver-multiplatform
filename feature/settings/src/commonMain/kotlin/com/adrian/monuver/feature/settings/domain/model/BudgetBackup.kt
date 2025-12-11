package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BudgetBackup(
    @SerialName("id") val id: Long = 0L,
    @SerialName("category") val category: Int,
    @SerialName("cycle") val cycle: Int,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    @SerialName("max_amount") val maxAmount: Long,
    @SerialName("used_amount") val usedAmount: Long,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("is_overflow_allowed") val isOverflowAllowed: Boolean,
    @SerialName("is_auto_update") val isAutoUpdate: Boolean
)
