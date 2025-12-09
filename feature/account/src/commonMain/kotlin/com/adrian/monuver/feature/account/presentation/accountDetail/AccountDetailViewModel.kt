package com.adrian.monuver.feature.account.presentation.accountDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.adrian.monuver.feature.account.domain.usecase.UpdateAccountStatusUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AccountDetailViewModel(
    private val updateAccountStatusUseCase: UpdateAccountStatusUseCase,
    savedStateHandle: SavedStateHandle,
    getAccountByIdUseCase: GetAccountByIdUseCase
) : ViewModel() {

    val account = getAccountByIdUseCase(savedStateHandle.toRoute<Account.Detail>().id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        viewModelScope.launch {
            _result.value = updateAccountStatusUseCase(
                accountId = accountId,
                isActive = isActive
            )
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}