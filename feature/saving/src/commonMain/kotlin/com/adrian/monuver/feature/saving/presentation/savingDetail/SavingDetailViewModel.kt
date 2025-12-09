package com.adrian.monuver.feature.saving.presentation.savingDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.util.toItem
import com.adrian.monuver.feature.saving.domain.usecase.CompleteSavingUseCase
import com.adrian.monuver.feature.saving.domain.usecase.DeleteSavingUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetAllTransactionsBySavingIdUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.adrian.monuver.core.presentation.navigation.Saving as SavingRoute

internal class SavingDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val deleteSavingUseCase: DeleteSavingUseCase,
    private val completeSavingUseCase: CompleteSavingUseCase,
    private val getSavingByIdUseCase: GetSavingByIdUseCase,
    private val getAllTransactionsBySavingIdUseCase: GetAllTransactionsBySavingIdUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<SavingDetailState?>(null)
    val state = _state
        .onStart {
            getInitialData()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun getInitialData() {
        val id = savedStateHandle.toRoute<SavingRoute.Detail>().id

        combine(
            getSavingByIdUseCase(id),
            getAllTransactionsBySavingIdUseCase(id).map { list -> list.map { it.toItem() } }
        ) { saving, transactions ->
            _state.value = SavingDetailState(
                id = saving?.id ?: 0,
                title = saving?.title ?: "",
                targetDate = saving?.targetDate ?: "2025-12-08",
                targetAmount = saving?.targetAmount ?: 0,
                currentAmount = saving?.currentAmount ?: 0,
                isActive = saving?.isActive ?: false,
                transactions = transactions
            )
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun deleteSaving(savingId: Long) {
        deleteSavingUseCase(savingId).onEach { progress ->
            _state.update {
                it?.copy(progress = progress)
            }
        }.launchIn(viewModelScope)
    }

    fun completeSaving(savingId: Long, savingName: String, savingAmount: Long) {
        viewModelScope.launch {
            _state.update {
                it?.copy(
                    result = completeSavingUseCase(
                        savingId = savingId,
                        savingName = savingName,
                        savingAmount = savingAmount
                    )
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