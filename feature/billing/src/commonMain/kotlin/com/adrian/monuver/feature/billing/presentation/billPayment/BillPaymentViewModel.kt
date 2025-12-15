package com.adrian.monuver.feature.billing.presentation.billPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.domain.usecase.GetCategoryRecommendationByTitleUseCase
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.feature.billing.domain.model.BillPayment
import com.adrian.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.adrian.monuver.feature.billing.domain.usecase.ProcessBillPaymentUseCase
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class BillPaymentViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val preferences: MonuverPreferences,
    private val getBillByIdUseCase: GetBillByIdUseCase,
    private val getCategoryRecommendationByTitleUseCase: GetCategoryRecommendationByTitleUseCase,
    private val processBillPaymentUseCase: ProcessBillPaymentUseCase,
    private val customDispatcher: CustomDispatcher,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BillPaymentState())
    val state = _state.asStateFlow()

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init { observeUseCases() }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeUseCases() {
        val id = savedStateHandle.toRoute<Billing.Payment>().id

        getBillByIdUseCase(id).onEach { bill ->
            bill?.let { bill ->
                _state.update {
                    it.copy(
                        bill = bill
                    )
                }
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)

        _state.map { it.title }
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { title ->
                if (title.isNotEmpty()) {
                    getCategoryRecommendationByTitleUseCase(title, TransactionType.EXPENSE)
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

    fun processBillPayment(bill: Bill, billPayment: BillPayment) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = processBillPaymentUseCase(bill, billPayment)
                )
            }
            preferences.setAccountDefault(
                id = billPayment.sourceId,
                name = billPayment.sourceName
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