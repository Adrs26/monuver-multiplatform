package com.adrian.monuver.feature.budgeting.presentation.addBudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.budgeting.domain.model.AddBudget
import com.adrian.monuver.feature.budgeting.domain.usecase.CreateBudgetUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddBudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase
) : ViewModel() {

    private val _category = MutableStateFlow(0)
    val category = _category.asStateFlow()

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun setCategory(category: Int) {
        _category.value = category
    }

    fun createNewBudgeting(budget: AddBudget) {
        viewModelScope.launch {
            _result.value = createBudgetUseCase(budget)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}