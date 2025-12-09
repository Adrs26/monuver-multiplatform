package com.adrian.monuver.feature.transaction.presentation.addTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.transaction.domain.model.AddTransaction
import com.adrian.monuver.feature.transaction.domain.usecase.CreateExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.CreateIncomeTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddTransactionViewModel(
    private val createIncomeTransactionUseCase: CreateIncomeTransactionUseCase,
    private val createExpenseTransactionUseCase: CreateExpenseTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setTransactionCategory(parentCategory: Int, childCategory: Int) {
        _state.update {
            it.copy(
                parentCategory = parentCategory,
                childCategory = childCategory
            )
        }
    }

    fun setTransactionAccount(accountId: Int, accountName: String) {
        _state.update {
            it.copy(
                accountId = accountId,
                accountName = accountName
            )
        }
    }

    fun createTransaction(addTransaction: AddTransaction) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = when (addTransaction.type) {
                        TransactionType.INCOME -> createIncomeTransactionUseCase(addTransaction)
                        TransactionType.EXPENSE -> createExpenseTransactionUseCase(addTransaction)
                        else -> DatabaseResultState.Initial
                    }
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