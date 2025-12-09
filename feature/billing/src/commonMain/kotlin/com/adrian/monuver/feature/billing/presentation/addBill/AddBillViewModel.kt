package com.adrian.monuver.feature.billing.presentation.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.billing.domain.model.AddBill
import com.adrian.monuver.feature.billing.domain.usecase.CreateBillUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddBillViewModel(
    private val createBillUseCase: CreateBillUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun createNewBill(bill: AddBill) {
        viewModelScope.launch {
            _result.value = createBillUseCase(bill)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}