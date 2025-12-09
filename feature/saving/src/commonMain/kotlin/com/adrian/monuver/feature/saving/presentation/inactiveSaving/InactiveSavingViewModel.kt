package com.adrian.monuver.feature.saving.presentation.inactiveSaving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.feature.saving.domain.usecase.GetAllInactiveSavingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class InactiveSavingViewModel(
    getAllInactiveSavingsUseCase: GetAllInactiveSavingsUseCase
) : ViewModel() {

    val savings = getAllInactiveSavingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}