package com.adrian.monuver.feature.transaction.domain.usecase

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class GetAllTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>> {
        return repository.getAllTransactions(
            query = query,
            type = type,
            month = month,
            year = year,
            scope = scope
        )
    }
}