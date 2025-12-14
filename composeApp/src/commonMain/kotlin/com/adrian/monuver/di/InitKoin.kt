package com.adrian.monuver.di

import com.adrian.monuver.core.data.di.coreModule
import com.adrian.monuver.core.data.di.platformCheckVersionModule
import com.adrian.monuver.core.data.di.platformDatabaseModule
import com.adrian.monuver.feature.account.di.accountModule
import com.adrian.monuver.feature.analytics.di.analyticsModule
import com.adrian.monuver.feature.billing.di.billingModule
import com.adrian.monuver.feature.billing.di.platformSchedulerModule
import com.adrian.monuver.feature.budgeting.di.budgetingModule
import com.adrian.monuver.feature.home.di.homeModule
import com.adrian.monuver.feature.saving.di.savingModule
import com.adrian.monuver.feature.settings.di.platformExportModule
import com.adrian.monuver.feature.settings.di.settingsModule
import com.adrian.monuver.feature.transaction.di.transactionModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) : KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            accountModule,
            analyticsModule,
            billingModule,
            budgetingModule,
            coreModule,
            homeModule,
            savingModule,
            settingsModule,
            transactionModule,
            platformCheckVersionModule(),
            platformDatabaseModule(),
            platformExportModule(),
            platformSchedulerModule(),
            platformStartingModule()
        )
    }
}