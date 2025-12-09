package com.adrian.monuver.core.domain.model

data class Saving(
    val id: Long = 0L,
    val title: String,
    val targetDate: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val isActive: Boolean
)