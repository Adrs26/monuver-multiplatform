package com.adrian.monuver.feature.transaction.presentation.addTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.domain.usecase.GetCategoryRecommendationByTitleUseCase
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.transaction.domain.model.AddTransaction
import com.adrian.monuver.feature.transaction.domain.usecase.CreateExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.CreateIncomeTransactionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val preferences: MonuverPreferences,
    private val getCategoryRecommendationByTitleUseCase: GetCategoryRecommendationByTitleUseCase,
    private val createIncomeTransactionUseCase: CreateIncomeTransactionUseCase,
    private val createExpenseTransactionUseCase: CreateExpenseTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    init { observeTitleChange() }

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeTitleChange() {
        _state.map { it.title}
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { title ->
                val type = savedStateHandle.toRoute<Transaction.Add>().type
                if (title.isNotEmpty()) {
                    getCategoryRecommendationByTitleUseCase(title, type)
                } else {
                    flowOf(TransactionCategoryRecommendation(0, 0))
                }
            }.onEach { categoryRecommendation ->
                _state.update {
                    it.copy(
                        parentCategory = categoryRecommendation.parentCategory,
                        childCategory = categoryRecommendation.childCategory
                    )
                }
            }.launchIn(viewModelScope)

        combine(
            preferences.accountDefaultId,
            preferences.accountDefaultName
        ) { accountId, accountName ->
            _state.update {
                it.copy(
                    accountId = accountId,
                    accountName = accountName
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setTransactionTitle(title: String) {
        _state.update {
            it.copy(title = title)
        }
    }

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
            preferences.setAccountDefault(
                id = addTransaction.accountId,
                name = addTransaction.accountName
            )
            delay(3.seconds)
            _state.update {
                it.copy(
                    result = DatabaseResultState.Initial
                )
            }
        }
    }
}