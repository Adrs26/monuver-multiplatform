package com.adrian.monuver.feature.transaction.di

import com.adrian.monuver.feature.transaction.data.repository.TransactionRepositoryImpl
import com.adrian.monuver.feature.transaction.domain.repository.TransactionRepository
import com.adrian.monuver.feature.transaction.domain.usecase.CreateExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.CreateIncomeTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.CreateTransferTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.DeleteExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.DeleteIncomeTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.UpdateExpenseTransactionUseCase
import com.adrian.monuver.feature.transaction.domain.usecase.UpdateIncomeTransactionUseCase
import com.adrian.monuver.feature.transaction.presentation.TransactionViewModel
import com.adrian.monuver.feature.transaction.presentation.addTransaction.AddTransactionViewModel
import com.adrian.monuver.feature.transaction.presentation.editTransaction.EditTransactionViewModel
import com.adrian.monuver.feature.transaction.presentation.transactionDetail.TransactionDetailViewModel
import com.adrian.monuver.feature.transaction.presentation.transfer.TransferViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionModule = module {
    singleOf(::TransactionRepositoryImpl).bind(TransactionRepository::class)

    factoryOf(::CreateExpenseTransactionUseCase)
    factoryOf(::CreateIncomeTransactionUseCase)
    factoryOf(::CreateTransferTransactionUseCase)
    factoryOf(::DeleteExpenseTransactionUseCase)
    factoryOf(::DeleteIncomeTransactionUseCase)
    factoryOf(::GetAllTransactionsUseCase)
    factoryOf(::GetTransactionByIdUseCase)
    factoryOf(::UpdateExpenseTransactionUseCase)
    factoryOf(::UpdateIncomeTransactionUseCase)

    viewModelOf(::TransactionViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::EditTransactionViewModel)
    viewModelOf(::TransactionDetailViewModel)
    viewModelOf(::TransferViewModel)
}