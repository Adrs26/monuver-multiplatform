package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository

internal class DeleteIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteIncomeTransaction(
            transactionId = transaction.id,
            sourceId = transaction.sourceId,
            amount = transaction.amount
        )
    }
}