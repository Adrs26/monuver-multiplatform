package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SavingBackup(
    @SerialName("id") val id: Long = 0L,
    @SerialName("title") val title: String,
    @SerialName("target_date") val targetDate: String,
    @SerialName("target_amount") val targetAmount: Long,
    @SerialName("current_amount") val currentAmount: Long,
    @SerialName("is_active") val isActive: Boolean
)
