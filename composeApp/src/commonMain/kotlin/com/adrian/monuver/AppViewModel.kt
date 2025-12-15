package com.adrian.monuver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class AppViewModel(
    private val preferences: MonuverPreferences,
    private val customDispatcher: CustomDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init { observePreferences() }

    private fun observePreferences() {
        combine(
            preferences.isFirstTime,
            preferences.themeState,
            preferences.isAuthenticationEnabled
        ) { isFirstTime, themeState, isAuthenticationEnabled ->
            _state.update {
                it.copy(
                    isFirstTime = isFirstTime,
                    themeState = themeState,
                    isAuthenticationEnabled = isAuthenticationEnabled
                )
            }
        }.flowOn(customDispatcher.default).launchIn(viewModelScope)
    }

    fun setIsFirstTimeToFalse() {
        viewModelScope.launch {
            preferences.setFirstTimeToFalse()
        }
    }

    fun setAuthenticationStatus(isAuthenticated: Boolean) {
        _state.update {
            it.copy(
                isAuthenticated = isAuthenticated
            )
        }
    }
}

internal data class AppState(
    val isFirstTime: Boolean = true,
    val themeState: ThemeState = ThemeState.System,
    val isAuthenticationEnabled: Boolean = false,
    val isAuthenticated: Boolean = false,
)