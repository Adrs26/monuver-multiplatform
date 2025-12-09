package com.adrian.monuver.feature.saving.presentation.withdraw

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal data class WithdrawState(
    val savingId: Long = 0,
    val savingName: String = "",
    val accountId: Int = 0,
    val accountName: String = "",
    val result: DatabaseResultState = DatabaseResultState.Initial
)
