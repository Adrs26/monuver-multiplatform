package com.adrian.monuver.feature.transaction.domain.model

internal data class Transfer(
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int,
    val destinationName: String,
    val date: String,
    val amount: Long
)