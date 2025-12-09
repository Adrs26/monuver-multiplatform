package com.adrian.monuver.feature.saving.presentation.deposit

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal data class DepositState(
    val savingId: Long = 0,
    val savingName: String = "",
    val accountId: Int = 0,
    val accountName: String = "",
    val result: DatabaseResultState = DatabaseResultState.Initial
)
