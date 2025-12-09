package com.adrian.monuver.feature.home.di

import com.adrian.monuver.feature.home.data.repository.HomeRepositoryImpl
import com.adrian.monuver.feature.home.domain.repository.HomeRepository
import com.adrian.monuver.feature.home.domain.usecase.GetActiveAccountBalanceUseCase
import com.adrian.monuver.feature.home.domain.usecase.GetRecentTransactionsUseCase
import com.adrian.monuver.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRepositoryImpl).bind(HomeRepository::class)

    factoryOf(::GetActiveAccountBalanceUseCase)
    factoryOf(::GetRecentTransactionsUseCase)

    viewModelOf(::HomeViewModel)
}