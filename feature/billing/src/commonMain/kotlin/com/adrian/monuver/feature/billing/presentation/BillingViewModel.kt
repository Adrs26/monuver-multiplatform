package com.adrian.monuver.feature.billing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.feature.billing.domain.model.BillItem
import com.adrian.monuver.feature.billing.domain.usecase.GetDueBillsUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetPaidBillsUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetPendingBillsUseCase
import com.adrian.monuver.feature.billing.presentation.components.ReminderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BillingViewModel(
    private val preferences: MonuverPreferences,
    private val getPendingBillsUseCase: GetPendingBillsUseCase,
    private val getDueBillsUseCase: GetDueBillsUseCase,
    private val getPaidBillsUseCase: GetPaidBillsUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(BillingState())
    val state = _state.asStateFlow()

    init { observeUseCases() }

    private fun observeUseCases() {
        combine(
            getPendingBillsUseCase().map { list -> list.map { it.toBillItem(1) } },
            getDueBillsUseCase().map { list -> list.map { it.toBillItem(2) } },
            getPaidBillsUseCase(viewModelScope).map { paging -> paging.map { it.toBillItem(3) } },
        ) { pendingBills, dueBills, paidBills ->
            _state.update {
                it.copy(
                    pendingBills = pendingBills,
                    dueBills = dueBills,
                    paidBills = flowOf(paidBills)
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)

        combine(
            preferences.reminderDaysBeforeDue,
            preferences.isReminderBeforeDueDayEnabled,
            preferences.isReminderForDueBillEnabled
        ) { reminderDaysBeforeDue, isReminderBeforeDueDayEnabled, isReminderForDueBillEnabled ->
            _state.update {
                it.copy(
                    reminderDaysBeforeDue = reminderDaysBeforeDue,
                    isReminderBeforeDueDayEnabled = isReminderBeforeDueDayEnabled,
                    isReminderForDueBillEnabled = isReminderForDueBillEnabled
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun Bill.toBillItem(status: Int) = BillItem(
        id = id,
        title = title,
        dueDate = dueDate,
        paidDate = paidDate ?: "",
        amount = amount,
        isRecurring = isRecurring,
        status = status,
        nowPaidPeriod = nowPaidPeriod
    )

    fun setReminderSettings(reminderState: ReminderState) {
        viewModelScope.launch {
            preferences.setReminderSettings(
                reminderDaysBeforeDue = reminderState.dueDayReminder,
                isReminderBeforeDueDayEnabled = reminderState.isReminderBeforeDueDayEnabled,
                isReminderForDueBillEnabled = reminderState.isReminderForDueBillEnabled
            )
        }
    }
}