package com.adrian.monuver.feature.billing.di

import com.adrian.monuver.feature.billing.worker.ReminderWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

actual fun platformSchedulerModule() = module {
    workerOf(::ReminderWorker)
}