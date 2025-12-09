package com.adrian.monuver.feature.saving.presentation.addSaving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.saving.domain.model.AddSaving
import com.adrian.monuver.feature.saving.domain.usecase.CreateSavingUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddSavingViewModel(
    private val createSavingUseCase: CreateSavingUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun createNewSaving(saving: AddSaving) {
        viewModelScope.launch {
            _result.value = createSavingUseCase(saving)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}