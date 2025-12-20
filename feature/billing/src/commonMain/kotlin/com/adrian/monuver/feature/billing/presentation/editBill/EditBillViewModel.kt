package com.adrian.monuver.feature.billing.presentation.editBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.feature.billing.domain.model.EditBill
import com.adrian.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.adrian.monuver.feature.billing.domain.usecase.UpdateBillUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class EditBillViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val updateBillUseCase: UpdateBillUseCase,
    private val getBillByIdUseCase: GetBillByIdUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<EditBillState?>(null)
    val state = _state
        .onStart {
            val id = savedStateHandle.toRoute<Billing.Edit>().id
            getBillById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    private fun getBillById(id: Long) {
        getBillByIdUseCase(id).onEach { bill ->
            bill?.let { bill ->
                _state.value = EditBillState(
                    id = bill.id,
                    parentId = bill.parentId,
                    title = bill.title,
                    date = bill.dueDate,
                    amount = bill.amount,
                    timeStamp = bill.timeStamp,
                    isRecurring = bill.isRecurring,
                    cycle = bill.cycle ?: 0,
                    period = bill.period ?: 0,
                    fixPeriod = bill.fixPeriod?.toString() ?: "",
                    nowPaidPeriod = bill.nowPaidPeriod,
                    isPaidBefore = bill.isPaidBefore
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun updateBill(bill: EditBill) {
        viewModelScope.launch {
            _result.value = updateBillUseCase(bill)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}