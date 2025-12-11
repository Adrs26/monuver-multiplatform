package com.adrian.monuver.feature.settings.presentation

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.common.ThemeState

internal data class SettingsState(
    val isNotificationEnabled: Boolean = false,
    val themeState: ThemeState = ThemeState.System,
    val isAuthenticationEnabled: Boolean = false,
    val result: DatabaseResultState = DatabaseResultState.Initial,
)