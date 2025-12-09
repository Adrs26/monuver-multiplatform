package com.adrian.monuver.feature.transaction.presentation.transfer

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal data class TransferState(
    val accountSourceId: Int = 0,
    val accountSourceName: String = "",
    val accountDestinationId: Int = 0,
    val accountDestinationName: String = "",
    val result: DatabaseResultState = DatabaseResultState.Initial
)