package com.adrian.monuver.feature.transaction.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.feature.transaction.domain.model.Transfer
import com.adrian.monuver.feature.transaction.domain.usecase.CreateTransferTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class TransferViewModel(
    private val createTransferTransactionUseCase: CreateTransferTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransferState())
    val state = _state.asStateFlow()

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val selectedAccounts = state.map { state ->
        listOf(state.accountSourceId, state.accountDestinationId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSourceAccount(sourceId: Int, sourceName: String) {
        _state.update {
            it.copy(
                accountSourceId = sourceId,
                accountSourceName = sourceName
            )
        }
    }

    fun setDestinationAccount(destinationId: Int, destinationName: String) {
        _state.update {
            it.copy(
                accountDestinationId = destinationId,
                accountDestinationName = destinationName
            )
        }
    }

    fun createNewTransfer(transfer: Transfer) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = createTransferTransactionUseCase(transfer)
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