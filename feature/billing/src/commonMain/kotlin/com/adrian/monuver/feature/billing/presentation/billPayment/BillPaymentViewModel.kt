package com.adrian.monuver.feature.billing.presentation.billPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.feature.billing.domain.model.BillPayment
import com.adrian.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.adrian.monuver.feature.billing.domain.usecase.ProcessBillPaymentUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class BillPaymentViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getBillByIdUseCase: GetBillByIdUseCase,
    private val processBillPaymentUseCase: ProcessBillPaymentUseCase,
    private val customDispatcher: CustomDispatcher,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BillPaymentState())
    val state = _state.asStateFlow()

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init { getBillById() }

    private fun getBillById() {
        val id = savedStateHandle.toRoute<Billing.Payment>().id

        getBillByIdUseCase(id).onEach { bill ->
            bill?.let { bill ->
                _state.update {
                    it.copy(
                        bill = bill
                    )
                }
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun setTransactionCategory(parentCategory: Int, childCategory: Int) {
        _state.update {
            it.copy(
                parentCategory = parentCategory,
                childCategory = childCategory
            )
        }
    }

    fun setTransactionAccount(accountId: Int, accountName: String) {
        _state.update {
            it.copy(
                accountId = accountId,
                accountName = accountName
            )
        }
    }

    fun processBillPayment(bill: Bill, billPayment: BillPayment) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = processBillPaymentUseCase(bill, billPayment)
                )
            }
            delay(3.seconds)
            _state.update {
                it.copy(
                    result = DatabaseResultState.Initial
                )
            }
        }
    }
}