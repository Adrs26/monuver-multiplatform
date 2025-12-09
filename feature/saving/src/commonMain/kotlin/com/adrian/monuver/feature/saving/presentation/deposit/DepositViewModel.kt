package com.adrian.monuver.feature.saving.presentation.deposit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.presentation.navigation.Saving
import com.adrian.monuver.feature.saving.domain.model.DepositWithdraw
import com.adrian.monuver.feature.saving.domain.usecase.CreateDepositTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class DepositViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createDepositTransactionUseCase: CreateDepositTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DepositState())
    val state = _state
        .onStart {
            val saving = savedStateHandle.toRoute<Saving.Deposit>()
            _state.update {
                it.copy(
                    savingId = saving.id ?: 0,
                    savingName = saving.name ?: ""
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setTransactionAccount(id: Int, name: String) {
        _state.update {
            it.copy(
                accountId = id,
                accountName = name
            )
        }
    }

    fun createNewTransaction(deposit: DepositWithdraw) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = createDepositTransactionUseCase(deposit)
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