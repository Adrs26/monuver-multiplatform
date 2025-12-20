package com.adrian.monuver.feature.saving.presentation.editSaving

internal data class EditSavingState(
    val id: Long = 0,
    val title: String = "",
    val targetDate: String = "",
    val targetAmount: Long = 0,
    val currentAmount: Long = 0,
    val isActive: Boolean = false
)
