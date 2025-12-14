package com.adrian.monuver.feature.settings.di

import com.adrian.monuver.feature.settings.data.manager.ExportManagerImpl
import com.adrian.monuver.feature.settings.domain.manager.ExportManager
import com.adrian.monuver.feature.settings.domain.usecase.ExportDataToPdfUseCase
import com.adrian.monuver.feature.settings.domain.usecase.ValidateDataUseCase
import com.adrian.monuver.feature.settings.presentation.export.ExportViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformExportModule() = module {
    singleOf(::ExportManagerImpl).bind(ExportManager::class)
    factoryOf(::ExportDataToPdfUseCase)
    factoryOf(::ValidateDataUseCase)
    viewModelOf(::ExportViewModel)
}