package com.adrian.monuver.feature.settings.domain.repository

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.settings.domain.model.DataBackup

internal interface SettingsRepository {

    suspend fun getAllDataForBackup(): DataBackup

    suspend fun deleteAllData()

    suspend fun getTransactionsInRangeByDateAsc(
        startDate: String,
        endDate: String
    ): List<Transaction>

    suspend fun getTransactionsInRangeByDateDesc(
        startDate: String,
        endDate: String
    ): List<Transaction>

    suspend fun getTransactionsInRangeByDateAscWithType(
        startDate: String,
        endDate: String
    ): List<Transaction>

    suspend fun getTransactionsInRangeByDateDescWithType(
        startDate: String,
        endDate: String
    ): List<Transaction>

    suspend fun getTotalIncomeTransactionInRange(
        startDate: String,
        endDate: String
    ): Long?

    suspend fun getTotalExpenseTransactionInRange(
        startDate: String,
        endDate: String
    ): Long?

    suspend fun restoreAllData(dataBackup: DataBackup)
}