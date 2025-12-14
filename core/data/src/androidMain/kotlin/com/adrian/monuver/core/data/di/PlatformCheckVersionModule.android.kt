package com.adrian.monuver.core.data.di

import com.adrian.monuver.core.domain.usecase.CheckAppVersionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual fun platformCheckVersionModule() = module {
    factoryOf(::CheckAppVersionUseCase)
}