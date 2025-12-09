package com.adrian.monuver.feature.saving.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.feature.saving.domain.usecase.GetAllActiveSavingsUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetTotalSavingCurrentAmountUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class SavingViewModel(
    getTotalSavingCurrentAmountUseCase: GetTotalSavingCurrentAmountUseCase,
    getAllActiveSavingsUseCase: GetAllActiveSavingsUseCase
) : ViewModel() {

    val totalCurrentAmount = getTotalSavingCurrentAmountUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val savings = getAllActiveSavingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}