package com.adrian.monuver.feature.settings.domain.usecase

import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository

internal class DeleteAllDataUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}