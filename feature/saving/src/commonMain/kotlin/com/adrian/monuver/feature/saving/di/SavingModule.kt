package com.adrian.monuver.feature.saving.di

import com.adrian.monuver.feature.saving.data.repository.SavingRepositoryImpl
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import com.adrian.monuver.feature.saving.domain.usecase.CompleteSavingUseCase
import com.adrian.monuver.feature.saving.domain.usecase.CreateDepositTransactionUseCase
import com.adrian.monuver.feature.saving.domain.usecase.CreateSavingUseCase
import com.adrian.monuver.feature.saving.domain.usecase.CreateWithdrawTransactionUseCase
import com.adrian.monuver.feature.saving.domain.usecase.DeleteSavingUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetAllActiveSavingsUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetAllInactiveSavingsUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetAllTransactionsBySavingIdUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import com.adrian.monuver.feature.saving.domain.usecase.GetTotalSavingCurrentAmountUseCase
import com.adrian.monuver.feature.saving.domain.usecase.UpdateSavingUseCase
import com.adrian.monuver.feature.saving.presentation.SavingViewModel
import com.adrian.monuver.feature.saving.presentation.addSaving.AddSavingViewModel
import com.adrian.monuver.feature.saving.presentation.deposit.DepositViewModel
import com.adrian.monuver.feature.saving.presentation.editSaving.EditSavingViewModel
import com.adrian.monuver.feature.saving.presentation.inactiveSaving.InactiveSavingViewModel
import com.adrian.monuver.feature.saving.presentation.savingDetail.SavingDetailViewModel
import com.adrian.monuver.feature.saving.presentation.withdraw.WithdrawViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val savingModule = module {
    singleOf(::SavingRepositoryImpl).bind(SavingRepository::class)

    factoryOf(::CompleteSavingUseCase)
    factoryOf(::CreateDepositTransactionUseCase)
    factoryOf(::CreateSavingUseCase)
    factoryOf(::CreateWithdrawTransactionUseCase)
    factoryOf(::DeleteSavingUseCase)
    factoryOf(::GetAllActiveSavingsUseCase)
    factoryOf(::GetAllInactiveSavingsUseCase)
    factoryOf(::GetAllTransactionsBySavingIdUseCase)
    factoryOf(::GetSavingByIdUseCase)
    factoryOf(::GetTotalSavingCurrentAmountUseCase)
    factoryOf(::UpdateSavingUseCase)

    viewModelOf(::SavingViewModel)
    viewModelOf(::AddSavingViewModel)
    viewModelOf(::DepositViewModel)
    viewModelOf(::EditSavingViewModel)
    viewModelOf(::InactiveSavingViewModel)
    viewModelOf(::SavingDetailViewModel)
    viewModelOf(::WithdrawViewModel)
}