package com.adrian.monuver.feature.billing.presentation

import androidx.paging.PagingData
import com.adrian.monuver.feature.billing.domain.model.BillItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class BillingState(
    val pendingBills: List<BillItem> = emptyList(),
    val dueBills: List<BillItem> = emptyList(),
    val paidBills: Flow<PagingData<BillItem>> = emptyFlow(),
    val reminderDaysBeforeDue: Int = 0,
    val isReminderBeforeDueDayEnabled: Boolean = false,
    val isReminderForDueBillEnabled: Boolean = false,
)
