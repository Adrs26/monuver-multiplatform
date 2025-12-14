package com.adrian.monuver.feature.settings.presentation.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.feature.settings.domain.common.ExportState
import com.adrian.monuver.feature.settings.domain.model.Export
import com.adrian.monuver.feature.settings.domain.usecase.ExportDataToPdfUseCase
import com.adrian.monuver.feature.settings.domain.usecase.ValidateDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class ExportViewModel(
    private val validateDataUseCase: ValidateDataUseCase,
    private val exportDataToPdfUseCase: ExportDataToPdfUseCase
) : ViewModel() {

    private val _status = MutableStateFlow<ExportState>(ExportState.Idle)
    val status = _status.asStateFlow()

    fun validateData(export: Export) {
        viewModelScope.launch {
            _status.value = validateDataUseCase(export)
            delay(3.seconds)
            _status.value = ExportState.Idle
        }
    }

    fun exportDataToPdf(stringUri: String, export: Export) {
        viewModelScope.launch {
            exportDataToPdfUseCase(stringUri, export).collect { status ->
                _status.value = status
            }
        }
    }
}