package com.adrian.monuver.feature.billing.domain.model

internal data class BillPayment(
    val title: String,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)