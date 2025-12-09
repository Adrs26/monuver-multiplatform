package com.adrian.monuver.feature.billing.di

import com.adrian.monuver.feature.billing.data.repository.BillRepositoryImpl
import com.adrian.monuver.feature.billing.domain.repository.BillRepository
import com.adrian.monuver.feature.billing.domain.usecase.CancelBillPaymentUseCase
import com.adrian.monuver.feature.billing.domain.usecase.CreateBillUseCase
import com.adrian.monuver.feature.billing.domain.usecase.DeleteBillUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetDueBillsUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetPaidBillsUseCase
import com.adrian.monuver.feature.billing.domain.usecase.GetPendingBillsUseCase
import com.adrian.monuver.feature.billing.domain.usecase.ProcessBillPaymentUseCase
import com.adrian.monuver.feature.billing.domain.usecase.UpdateBillUseCase
import com.adrian.monuver.feature.billing.presentation.BillingViewModel
import com.adrian.monuver.feature.billing.presentation.addBill.AddBillViewModel
import com.adrian.monuver.feature.billing.presentation.billDetail.BillDetailViewModel
import com.adrian.monuver.feature.billing.presentation.editBill.EditBillViewModel
import com.adrian.monuver.feature.billing.presentation.billPayment.BillPaymentViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val billingModule = module {
    singleOf(::BillRepositoryImpl).bind(BillRepository::class)

    factoryOf(::CancelBillPaymentUseCase)
    factoryOf(::CreateBillUseCase)
    factoryOf(::DeleteBillUseCase)
    factoryOf(::GetBillByIdUseCase)
    factoryOf(::GetPendingBillsUseCase)
    factoryOf(::GetDueBillsUseCase)
    factoryOf(::GetPaidBillsUseCase)
    factoryOf(::ProcessBillPaymentUseCase)
    factoryOf(::UpdateBillUseCase)

    viewModelOf(::BillingViewModel)
    viewModelOf(::AddBillViewModel)
    viewModelOf(::BillDetailViewModel)
    viewModelOf(::EditBillViewModel)
    viewModelOf(::BillPaymentViewModel)

//    workerOf(::ReminderWorker)
}