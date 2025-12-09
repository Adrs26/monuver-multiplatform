package com.adrian.monuver.feature.budgeting.di

import com.adrian.monuver.feature.budgeting.data.BudgetRepositoryImpl
import com.adrian.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.adrian.monuver.feature.budgeting.domain.usecase.CreateBudgetUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.DeleteBudgetUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetAllActiveBudgetsUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetAllInactiveBudgetsUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.GetTransactionsByCategoryAndDateRangeUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.HandleExpiredBudgetsUseCase
import com.adrian.monuver.feature.budgeting.domain.usecase.UpdateBudgetUseCase
import com.adrian.monuver.feature.budgeting.presentation.BudgetingViewModel
import com.adrian.monuver.feature.budgeting.presentation.addBudget.AddBudgetViewModel
import com.adrian.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailViewModel
import com.adrian.monuver.feature.budgeting.presentation.editBudget.EditBudgetViewModel
import com.adrian.monuver.feature.budgeting.presentation.inactiveBudget.InactiveBudgetViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val budgetingModule = module {
    singleOf(::BudgetRepositoryImpl).bind(BudgetRepository::class)

    factoryOf(::CreateBudgetUseCase)
    factoryOf(::DeleteBudgetUseCase)
    factoryOf(::GetAllActiveBudgetsUseCase)
    factoryOf(::GetAllInactiveBudgetsUseCase)
    factoryOf(::GetBudgetByIdUseCase)
    factoryOf(::GetTransactionsByCategoryAndDateRangeUseCase)
    factoryOf(::HandleExpiredBudgetsUseCase)
    factoryOf(::UpdateBudgetUseCase)

    viewModelOf(::BudgetingViewModel)
    viewModelOf(::AddBudgetViewModel)
    viewModelOf(::BudgetDetailViewModel)
    viewModelOf(::EditBudgetViewModel)
    viewModelOf(::InactiveBudgetViewModel)
}