package com.adrian.monuver.feature.settings.domain.manager

import com.adrian.monuver.feature.settings.domain.model.DataBackup

internal interface BackupRestoreManager {

    suspend fun backupData(data: DataBackup): Boolean

    suspend fun restoreData(): DataBackup
}