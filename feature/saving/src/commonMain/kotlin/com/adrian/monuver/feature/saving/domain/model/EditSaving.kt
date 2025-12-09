package com.adrian.monuver.feature.saving.domain.model

internal data class EditSaving(
    val id: Long,
    val title: String,
    val targetDate: String,
    val currentAmount: Long,
    val targetAmount: Long
)