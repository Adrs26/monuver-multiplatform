package com.adrian.monuver.feature.settings.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.settings.domain.manager.BackupRestoreManager
import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository

internal class RestoreDataUseCase(
    private val manager: BackupRestoreManager,
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): DatabaseResultState {
        return try {
            val backupData = manager.restoreData()
            repository.restoreAllData(backupData)
            DatabaseResultState.RestoreDataSuccess
        } catch (_: Exception) {
            DatabaseResultState.RestoreDataFailed
        }
    }
}