package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountBackup(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String,
    @SerialName("type") val type: Int,
    @SerialName("balance") val balance: Long,
    @SerialName("is_active") val isActive: Boolean
)