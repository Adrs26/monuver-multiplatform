package com.adrian.monuver.feature.saving.domain.model

internal data class AddSaving(
    val title: String,
    val targetDate: String,
    val targetAmount: Long
)