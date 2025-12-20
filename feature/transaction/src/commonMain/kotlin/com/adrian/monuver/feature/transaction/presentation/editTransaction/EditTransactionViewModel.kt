package com.adrian.monuver.feature.transaction.presentation.editTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.transaction.domain.model.EditTransaction
import com.adrian.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.UpdateExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.UpdateIncomeTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateIncomeTransactionUseCase: UpdateIncomeTransactionUseCase,
    private val updateExpenseTransactionUseCase: UpdateExpenseTransactionUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<EditTransactionState?>(null)
    val state = _state
        .onStart {
            val id = savedStateHandle.toRoute<Transaction.Edit>().id
            getTransactionById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    private fun getTransactionById(id: Long) {
        getTransactionByIdUseCase(id).onEach { transaction ->
            transaction?.let { transaction ->
                _state.value = EditTransactionState(
                    id = transaction.id,
                    title = transaction.title,
                    type = transaction.type,
                    parentCategory = transaction.parentCategory,
                    initialParentCategory = transaction.parentCategory,
                    childCategory = transaction.childCategory,
                    date = transaction.date,
                    initialDate = transaction.date,
                    amount = transaction.amount,
                    initialAmount = transaction.amount,
                    accountId = transaction.sourceId,
                    accountName = transaction.sourceName,
                    isLocked = transaction.isLocked
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun setTransactionCategory(parentCategory: Int, childCategory: Int) {
        _state.update {
            it?.copy(
                parentCategory = parentCategory,
                childCategory = childCategory
            )
        }
    }

    fun updateTransaction(transaction: EditTransaction) {
        viewModelScope.launch {
            _result.value = when (transaction.type) {
                TransactionType.INCOME -> updateIncomeTransactionUseCase(transaction)
                TransactionType.EXPENSE -> updateExpenseTransactionUseCase(transaction)
                else -> DatabaseResultState.Initial
            }
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}