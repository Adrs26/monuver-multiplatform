package com.adrian.monuver.feature.settings.di

import com.adrian.monuver.feature.settings.data.manager.BackupRestoreManagerImpl
import com.adrian.monuver.feature.settings.data.repository.SettingsRepositoryImpl
import com.adrian.monuver.feature.settings.domain.manager.BackupRestoreManager
import com.adrian.monuver.feature.settings.domain.repository.SettingsRepository
import com.adrian.monuver.feature.settings.domain.usecase.BackupDataUseCase
import com.adrian.monuver.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.adrian.monuver.feature.settings.domain.usecase.RestoreDataUseCase
import com.adrian.monuver.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    singleOf(::SettingsRepositoryImpl).bind(SettingsRepository::class)
    singleOf(::BackupRestoreManagerImpl).bind(BackupRestoreManager::class)

    factoryOf(::BackupDataUseCase)
    factoryOf(::DeleteAllDataUseCase)
    factoryOf(::RestoreDataUseCase)

    viewModelOf(::SettingsViewModel)
}

expect fun platformExportModule(): Module