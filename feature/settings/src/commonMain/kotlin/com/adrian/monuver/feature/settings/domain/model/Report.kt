package com.adrian.monuver.feature.settings.domain.model

import com.adrian.monuver.core.domain.model.Transaction

internal data class Report(
    val reportName: String,
    val username: String,
    val startDate: String,
    val endDate: String,
    val commonTransactions: List<Transaction>,
    val transferTransactions: List<Transaction>,
    val totalIncome: Long,
    val totalExpense: Long,
)
