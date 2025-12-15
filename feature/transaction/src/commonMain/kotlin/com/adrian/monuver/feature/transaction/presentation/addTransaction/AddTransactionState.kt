package com.adrian.monuver.feature.transaction.presentation.addTransaction

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal data class AddTransactionState(
    val title: String = "",
    val parentCategory: Int = 0,
    val childCategory: Int = 0,
    val accountId: Int = 0,
    val accountName: String = "",
    val result: DatabaseResultState = DatabaseResultState.Initial
)
