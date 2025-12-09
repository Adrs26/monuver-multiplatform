package com.adrian.monuver.feature.analytics.di

import com.adrian.monuver.feature.analytics.data.repository.AnalyticsRepositoryImpl
import com.adrian.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.adrian.monuver.feature.analytics.domain.usecase.GetAllTransactionsByCategoryUseCase
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionBalanceSummaryUseCase
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionCategorySummaryUseCase
import com.adrian.monuver.feature.analytics.domain.usecase.GetTransactionSummaryUseCase
import com.adrian.monuver.feature.analytics.presentation.AnalyticsViewModel
import com.adrian.monuver.feature.analytics.presentation.analyticsTransaction.AnalyticsTransactionViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::AnalyticsRepositoryImpl).bind(AnalyticsRepository::class)

    factoryOf(::GetAllTransactionsByCategoryUseCase)
    factoryOf(::GetTransactionBalanceSummaryUseCase)
    factoryOf(::GetTransactionCategorySummaryUseCase)
    factoryOf(::GetTransactionSummaryUseCase)

    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::AnalyticsTransactionViewModel)
}