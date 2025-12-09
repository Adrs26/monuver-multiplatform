package com.adrian.monuver.feature.billing.presentation

import com.adrian.monuver.feature.billing.presentation.components.ReminderState

internal sealed interface BillingAction {
    data object NavigateBack : BillingAction
    data object NavigateToAddBill : BillingAction
    data class  NavigateToBillDetail(val billId: Long) : BillingAction
    data class  SettingsApply(val reminderState: ReminderState) : BillingAction
}