package com.adrian.monuver.feature.settings.data.manager

import com.adrian.monuver.feature.settings.domain.manager.BackupRestoreManager
import com.adrian.monuver.feature.settings.domain.model.DataBackup
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class BackupRestoreManagerImpl : BackupRestoreManager {

    @OptIn(ExperimentalTime::class)
    override suspend fun backupData(data: DataBackup): Boolean {
        return try {
            val currentMillis = Clock.System.now().toEpochMilliseconds()

            val file = FileKit.openFileSaver(
                suggestedName = "monuver_backup_${currentMillis}",
                extension = "json"
            )

            if (file == null) {
                return false
            }

            val json = Json.encodeToString(data)
            file.writeString(json)

            true
        } catch (_: Exception) {
            false
        }
    }


    override suspend fun restoreData(): DataBackup {
        try {
            val file = FileKit.openFilePicker(type = FileKitType.File(extension = "json"))

            if (file != null) {
                val dataBackup = Json.decodeFromString<DataBackup>(file.readString())
                return dataBackup
            } else {
                throw Exception("File not found")
            }
        } catch (_: Exception) {
            throw Exception("File not found")
        }
    }
}