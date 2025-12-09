package com.adrian.monuver.feature.saving.presentation.savingDetail

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.feature.saving.domain.common.DeleteState

internal data class SavingDetailState(
    val id: Long = 0,
    val title: String = "",
    val targetDate: String = "",
    val targetAmount: Long = 0,
    val currentAmount: Long = 0,
    val isActive: Boolean = false,
    val transactions: List<TransactionItem> = emptyList(),
    val progress: DeleteState = DeleteState.Idle,
    val result: DatabaseResultState = DatabaseResultState.Initial,
)
