package com.adrian.monuver.di

import com.adrian.monuver.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual fun platformStartingModule() = module {
    viewModelOf(::MainViewModel)
}