package com.adrian.monuver.feature.saving.domain.model

internal data class DepositWithdraw(
    val date: String,
    val amount: Long,
    val accountId: Int,
    val accountName: String,
    val savingId: Long,
    val savingName: String
)