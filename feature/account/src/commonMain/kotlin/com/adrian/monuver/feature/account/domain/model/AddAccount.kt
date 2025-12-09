package com.adrian.monuver.feature.account.domain.model

internal data class AddAccount(
    val name: String,
    val type: Int,
    val balance: Long
)