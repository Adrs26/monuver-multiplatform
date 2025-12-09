package com.adrian.monuver.feature.account.presentation.addAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.account.domain.model.AddAccount
import com.adrian.monuver.feature.account.domain.usecase.CreateAccountUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class AddAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _accountType = MutableStateFlow(0)
    val accountType = _accountType.asStateFlow()

    private val _result = MutableStateFlow<DatabaseResultState>(DatabaseResultState.Initial)
    val result = _result.asStateFlow()

    fun setAccountType(type: Int) {
        _accountType.value = type
    }

    fun createNewAccount(accountState: AddAccount) {
        viewModelScope.launch {
            _result.value = createAccountUseCase(accountState)
            delay(3.seconds)
            _result.value = DatabaseResultState.Initial
        }
    }
}