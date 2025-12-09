package com.adrian.monuver.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.adrian.monuver.core.data.database.dao.AccountDao
import com.adrian.monuver.core.data.database.dao.BillDao
import com.adrian.monuver.core.data.database.dao.BudgetDao
import com.adrian.monuver.core.data.database.dao.SavingDao
import com.adrian.monuver.core.data.database.dao.TransactionDao
import com.adrian.monuver.core.data.database.entity.room.AccountEntity
import com.adrian.monuver.core.data.database.entity.room.BillEntity
import com.adrian.monuver.core.data.database.entity.room.BudgetEntity
import com.adrian.monuver.core.data.database.entity.room.SavingEntity
import com.adrian.monuver.core.data.database.entity.room.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        SavingEntity::class,
        BillEntity::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(MonuverDatabaseConstructor::class)
abstract class MonuverDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun saveDao(): SavingDao
    abstract fun billDao(): BillDao
}

@Suppress("KotlinNoActualForExpect")
expect object MonuverDatabaseConstructor : RoomDatabaseConstructor<MonuverDatabase> {
    override fun initialize(): MonuverDatabase
}