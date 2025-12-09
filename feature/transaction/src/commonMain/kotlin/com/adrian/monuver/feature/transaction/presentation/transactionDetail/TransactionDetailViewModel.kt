package com.adrian.monuver.feature.transaction.presentation.transactionDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.transaction.domain.usecase.DeleteExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.DeleteIncomeTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.adrian.monuver.core.presentation.navigation.Transaction as TransactionRoute

internal class TransactionDetailViewModel(
    private val deleteIncomeTransactionUseCase: DeleteIncomeTransactionUseCase,
    private val deleteExpenseTransactionUseCase: DeleteExpenseTransactionUseCase,
    savedStateHandle: SavedStateHandle,
    getTransactionByIdUseCase: GetTransactionByIdUseCase
) : ViewModel() {

    val transaction = getTransactionByIdUseCase(
        savedStateHandle.toRoute<TransactionRoute.Detail>().id
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            when (transaction.type) {
                TransactionType.INCOME -> deleteIncomeTransactionUseCase(transaction)
                TransactionType.EXPENSE -> deleteExpenseTransactionUseCase(transaction)
                else -> Unit
            }
        }
    }
}