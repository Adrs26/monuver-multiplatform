package com.adrian.monuver.feature.billing.presentation.billDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.feature.billing.domain.usecase.CancelBillPaymentUseCase
import com.adrian.monuver.feature.billing.domain.usecase.DeleteBillUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class BillDetailViewModel(
    private val deleteBillUseCase: DeleteBillUseCase,
    private val cancelBillPaymentUseCase: CancelBillPaymentUseCase,
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase
) : ViewModel() {

    val bill = getBillByIdUseCase(savedStateHandle.toRoute<Billing.Detail>().id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun onAction(action: BillDetailAction) {
        when (action) {
            is BillDetailAction.CancelBillPayment -> cancelBillPayment(action.billId)
            is BillDetailAction.RemoveBill -> deleteBill(action.billId)
            else -> Unit
        }
    }

    private fun cancelBillPayment(billId: Long) {
        viewModelScope.launch {
            _result.value = cancelBillPaymentUseCase(billId)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }

    private fun deleteBill(billId: Long) {
        viewModelScope.launch {
            deleteBillUseCase(billId)
        }
    }
}