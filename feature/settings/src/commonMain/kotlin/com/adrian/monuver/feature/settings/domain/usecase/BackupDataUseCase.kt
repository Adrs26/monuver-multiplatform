package com.adrian.monuver.feature.settings.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.settings.domain.manager.BackupRestoreManager
import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository

internal class BackupDataUseCase(
    private val manager: BackupRestoreManager,
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): DatabaseResultState {
        return try {
            val backupData = repository.getAllDataForBackup()

            if (manager.backupData(backupData)) {
                DatabaseResultState.BackupDataSuccess
            } else {
                DatabaseResultState.BackupDataFailed
            }
        } catch (_: Exception) {
            DatabaseResultState.BackupDataFailed
        }
    }
}