package com.adrian.monuver.feature.billing.domain.usecase

import androidx.paging.PagingData
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class GetPaidBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<Bill>> {
        return repository.getPaidBills(scope)
    }
}