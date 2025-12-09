package com.adrian.monuver.feature.transaction.domain.model

internal data class EditTransaction(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val initialParentCategory: Int,
    val date: String,
    val initialDate: String,
    val amount: Long,
    val initialAmount: Long,
    val sourceId: Int,
    val sourceName: String,
    val isLocked: Boolean
)