package com.adrian.monuver.feature.transaction.domain.model

internal data class AddTransaction(
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val accountId: Int,
    val accountName: String
)