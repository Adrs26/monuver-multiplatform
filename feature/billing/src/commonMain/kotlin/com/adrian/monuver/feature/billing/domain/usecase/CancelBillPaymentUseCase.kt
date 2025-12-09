package com.adrian.monuver.feature.billing.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.billing.domain.repository.BillRepository

internal class CancelBillPaymentUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billId: Long): DatabaseResultState {
        repository.cancelBillPayment(billId)
        return DatabaseResultState.CancelBillSuccess
    }
}