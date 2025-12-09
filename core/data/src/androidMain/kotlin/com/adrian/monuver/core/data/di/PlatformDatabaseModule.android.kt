package com.adrian.monuver.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.adrian.monuver.core.data.database.MonuverDatabase
import com.adrian.monuver.core.data.database.getDatabaseBuilder
import com.adrian.monuver.core.data.datastore.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformDatabaseModule() = module {
    single<MonuverDatabase> { getDatabaseBuilder(get()) }
    single<DataStore<Preferences>> { createDataStore(androidContext()) }
}