package com.adrian.monuver.core.data.di

import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.data.repository.CoreRepositoryImpl
import com.adrian.monuver.core.domain.common.CustomDispatcher
import com.adrian.monuver.core.domain.common.CustomDispatcherImpl
import com.adrian.monuver.core.domain.repository.CoreRepository
import com.adrian.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.adrian.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.adrian.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.adrian.monuver.core.domain.usecase.GetUnpaidBillsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single { get<MonuverDatabase>().transactionDao() }
    single { get<MonuverDatabase>().accountDao() }
    single { get<MonuverDatabase>().budgetDao() }
    single { get<MonuverDatabase>().saveDao() }
    single { get<MonuverDatabase>().billDao() }

    singleOf(::MonuverPreferences)
    singleOf(::CoreRepositoryImpl).bind(CoreRepository::class)
    singleOf(::CustomDispatcherImpl).bind(CustomDispatcher::class)

    factoryOf(::GetActiveAccountsUseCase)
    factoryOf(::GetBudgetSummaryUseCase)
    factoryOf(::GetDistinctTransactionYearsUseCase)
    factoryOf(::GetUnpaidBillsUseCase)
}