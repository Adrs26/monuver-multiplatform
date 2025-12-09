package com.adrian.monuver.feature.account.di

import com.adrian.monuver.feature.account.data.repository.AccountRepositoryImpl
import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import com.adrian.monuver.feature.account.domain.usecase.CreateAccountUseCase
import com.adrian.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.adrian.monuver.feature.account.domain.usecase.GetAllAccountsUseCase
import com.adrian.monuver.feature.account.domain.usecase.GetTotalAccountBalanceUseCase
import com.adrian.monuver.feature.account.domain.usecase.UpdateAccountStatusUseCase
import com.adrian.monuver.feature.account.domain.usecase.UpdateAccountUseCase
import com.adrian.monuver.feature.account.presentation.AccountViewModel
import com.adrian.monuver.feature.account.presentation.accountDetail.AccountDetailViewModel
import com.adrian.monuver.feature.account.presentation.addAccount.AddAccountViewModel
import com.adrian.monuver.feature.account.presentation.editAccount.EditAccountViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val accountModule = module {
    singleOf(::AccountRepositoryImpl).bind(AccountRepository::class)

    factoryOf(::CreateAccountUseCase)
    factoryOf(::GetAccountByIdUseCase)
    factoryOf(::GetAllAccountsUseCase)
    factoryOf(::GetTotalAccountBalanceUseCase)
    factoryOf(::UpdateAccountStatusUseCase)
    factoryOf(::UpdateAccountUseCase)

    viewModelOf(::AccountViewModel)
    viewModelOf(::AccountDetailViewModel)
    viewModelOf(::AddAccountViewModel)
    viewModelOf(::EditAccountViewModel)
}