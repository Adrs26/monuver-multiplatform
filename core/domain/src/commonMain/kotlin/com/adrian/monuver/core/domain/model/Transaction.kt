package com.adrian.monuver.core.domain.model

data class Transaction(
    val id: Long = 0,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val timeStamp: Long,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int? = null,
    val destinationName: String? = null,
    val saveId: Long? = null,
    val billId: Long? = null,
    val isLocked: Boolean,
    val isSpecialCase: Boolean
)