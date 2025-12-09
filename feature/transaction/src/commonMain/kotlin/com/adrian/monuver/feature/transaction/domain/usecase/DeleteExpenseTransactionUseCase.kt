package com.adrian.monuver.feature.transaction.domain.usecase

import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository

internal class DeleteExpenseTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteExpenseTransaction(
            transactionId = transaction.id,
            parentCategory = transaction.parentCategory,
            date = transaction.date,
            sourceId = transaction.sourceId,
            amount = transaction.amount
        )
    }
}