package com.adrian.monuver.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.common.ThemeState
import com.adrian.monuver.feature.settings.domain.common.ExportState
import com.adrian.monuver.feature.settings.domain.usecase.BackupDataUseCase
import com.adrian.monuver.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.adrian.monuver.feature.settings.domain.usecase.RestoreDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class SettingsViewModel(
    private val preferences: MonuverPreferences,
//    private val exportDataToPdfUseCase: ExportDataToPdfUseCase,
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _status = MutableStateFlow<ExportState>(ExportState.Idle)
    val status = _status.asStateFlow()

    init { observePreferences() }

    private fun observePreferences() {
        combine(
            preferences.isNotificationEnabled,
            preferences.themeState,
            preferences.isAuthenticationEnabled
        ) { isNotificationEnabled, themeState, isAuthenticationEnabled ->
            _state.update {
                it.copy(
                    isNotificationEnabled = isNotificationEnabled,
                    themeState = themeState,
                    isAuthenticationEnabled = isAuthenticationEnabled
                )
            }
            println("isNotificationEnabled: $isNotificationEnabled")
            println("themeState: $themeState")
        }.launchIn(viewModelScope)
    }

    fun setNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setNotification(isEnabled)
        }
    }

    fun changeTheme(themeState: ThemeState) {
        viewModelScope.launch {
            preferences.setThemeState(themeState)
        }
    }

    fun backupData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = backupDataUseCase()
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

    fun restoreData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    result = restoreDataUseCase()
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

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllDataUseCase()
        }
    }

    fun setAuthentication(isEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setAuthentication(isEnabled)
        }
    }
}