package com.adrian.monuver.feature.account.domain.model

internal data class EditAccount(
    val id: Int,
    val name: String,
    val type: Int,
    val balance: Long
)