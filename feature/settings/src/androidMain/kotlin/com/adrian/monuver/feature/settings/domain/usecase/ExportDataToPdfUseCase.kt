package com.adrian.monuver.feature.settings.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.settings.domain.common.ExportState
import com.adrian.monuver.feature.settings.domain.manager.ExportManager
import com.adrian.monuver.feature.settings.domain.model.Export
import com.adrian.monuver.feature.settings.domain.model.Report
import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

internal class ExportDataToPdfUseCase(
    private val manager: ExportManager,
    private val repository: SettingsRepository
) {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(
        stringUri: String,
        export: Export
    ): Flow<ExportState> {
        return flow {
            emit(ExportState.Progress)

            delay(3.seconds)

            val transactions = when (export.sortType) {
                1 -> {
                    if (export.isTransactionGrouped) {
                        repository.getTransactionsInRangeByDateAscWithType(
                            startDate = export.startDate,
                            endDate = export.endDate
                        )
                    } else {
                        repository.getTransactionsInRangeByDateAsc(
                            startDate = export.startDate,
                            endDate = export.endDate
                        )
                    }
                }
                2 -> {
                    if (export.isTransactionGrouped) {
                        repository.getTransactionsInRangeByDateDescWithType(
                            startDate = export.startDate,
                            endDate = export.endDate
                        )
                    } else {
                        repository.getTransactionsInRangeByDateDesc(
                            startDate = export.startDate,
                            endDate = export.endDate
                        )
                    }
                }
                else -> emptyList()
            }

            val commonTransactions = transactions.filter {
                it.type == TransactionType.INCOME || it.type == TransactionType.EXPENSE
            }

            val transferTransactions = if (export.isTransferIncluded) {
                transactions.filter { it.type == TransactionType.TRANSFER }
            } else {
                emptyList()
            }

            val totalIncome = repository.getTotalIncomeTransactionInRange(
                startDate = export.startDate,
                endDate = export.endDate
            )
            val totalExpense = repository.getTotalExpenseTransactionInRange(
                startDate = export.startDate,
                endDate = export.endDate
            )

            try {
                manager.exportToPdf(
                    stringUri = stringUri,
                    report = Report(
                        reportName = export.title,
                        username = export.username,
                        startDate = export.startDate,
                        endDate = export.endDate,
                        commonTransactions = commonTransactions,
                        transferTransactions = transferTransactions,
                        totalIncome = totalIncome ?: 0,
                        totalExpense = totalExpense ?: 0
                    )
                )

                emit(ExportState.Success)
            } catch (_: Exception) {
                emit(ExportState.Error(DatabaseResultState.ExportDataFailed))
            }
        }
    }
}