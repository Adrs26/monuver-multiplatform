package com.adrian.monuver.feature.transaction.presentation.editTransaction

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal data class EditTransactionState(
    val id: Long = 0,
    val title: String = "",
    val type: Int = 0,
    val parentCategory: Int = 0,
    val initialParentCategory: Int = 0,
    val childCategory: Int = 0,
    val date: String = "",
    val initialDate: String = "",
    val amount: Long = 0,
    val initialAmount: Long = 0,
    val accountId: Int = 0,
    val accountName: String = "",
    val isLocked: Boolean = false,
    val result: DatabaseResultState = DatabaseResultState.Initial
)
