package com.adrian.monuver.feature.settings.presentation

import com.adrian.monuver.core.domain.common.ThemeState

internal interface SettingsAction {
    data object NavigateBack : SettingsAction
    data class  NotificationEnableChange(val isEnabled: Boolean) : SettingsAction
    data class  ThemeChange(val themeState: ThemeState) : SettingsAction
    data object NavigateToExport : SettingsAction
    data object BackupData : SettingsAction
    data object RestoreData : SettingsAction
    data object RemoveAllData : SettingsAction
    data class  AuthenticationEnableChange(val isEnabled: Boolean) : SettingsAction
}