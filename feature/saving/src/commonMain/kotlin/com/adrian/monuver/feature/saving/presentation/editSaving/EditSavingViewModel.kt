package com.adrian.monuver.feature.saving.presentation.editSaving

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.navigation.Saving
import com.adrian.monuver.feature.saving.domain.model.EditSaving
import com.adrian.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import com.adrian.monuver.feature.saving.domain.usecase.UpdateSavingUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class EditSavingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getSavingByIdUseCase: GetSavingByIdUseCase,
    private val updateSavingUseCase: UpdateSavingUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<EditSavingState?>(null)
    val state = _state
        .onStart {
            val id = savedStateHandle.toRoute<Saving.Edit>().id
            getSavingById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun getSavingById(id: Long) {
        getSavingByIdUseCase(id).onEach { saving ->
            saving?.let { saving ->
                _state.value = EditSavingState(
                    id = saving.id,
                    title = saving.title,
                    targetDate = saving.targetDate,
                    targetAmount = saving.targetAmount,
                    currentAmount = saving.currentAmount,
                    isActive = saving.isActive
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun updateSaving(saving: EditSaving) {
        viewModelScope.launch {
            _state.update {
                it?.copy(
                    result = updateSavingUseCase(saving)
                )
            }
            delay(3.seconds)
            _state.update {
                it?.copy(
                    result = DatabaseResultState.Initial
                )
            }
        }
    }
}