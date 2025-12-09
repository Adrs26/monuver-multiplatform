package com.adrian.monuver.feature.account.presentation.editAccount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.feature.account.domain.model.EditAccount
import com.adrian.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.adrian.monuver.feature.account.domain.usecase.UpdateAccountUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.adrian.monuver.core.presentation.navigation.Account as AccountRoute

internal class EditAccountViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase
) : ViewModel() {

    private val _account = MutableStateFlow<Account?>(null)
    val account = _account
        .onStart {
            val id = savedStateHandle.toRoute<AccountRoute.Edit>().id
            getAccountById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    private fun getAccountById(id: Int) {
        getAccountByIdUseCase(id).onEach { account ->
            _account.value = account
        }.launchIn(viewModelScope)
    }

    fun changeAccountType(type: Int) {
        _account.update { account ->
            account?.copy(type = type)
        }
    }

    fun updateAccount(account: EditAccount) {
        viewModelScope.launch {
            _result.value = updateAccountUseCase(account)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}