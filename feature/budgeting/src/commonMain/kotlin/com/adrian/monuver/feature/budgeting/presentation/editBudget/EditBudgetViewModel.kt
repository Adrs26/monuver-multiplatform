package com.adrian.monuver.feature.budgeting.presentation.editBudget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.feature.budgeting.domain.model.EditBudget
import com.adrian.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.UpdateBudgetUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class EditBudgetViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<EditBudgetState?>(null)
    val state = _state
        .onStart {
            val id = savedStateHandle.toRoute<Budget.Edit>().id
            getBudgetById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    private fun getBudgetById(id: Long) {
        getBudgetByIdUseCase(id).onEach { budget ->
            budget?.let { budget ->
                _state.value = EditBudgetState(
                    id = budget.id,
                    category = budget.category,
                    maxAmount = budget.maxAmount,
                    cycle = budget.cycle,
                    startDate = budget.startDate,
                    endDate = budget.endDate,
                    isOverflowAllowed = budget.isOverflowAllowed,
                    isAutoUpdate = budget.isAutoUpdate
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun updateBudget(budget: EditBudget) {
        viewModelScope.launch {
            _result.value = updateBudgetUseCase(budget)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}