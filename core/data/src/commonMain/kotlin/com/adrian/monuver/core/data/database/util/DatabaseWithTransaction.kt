package com.adrian.monuver.core.data.database.util

import androidx.room.RoomDatabase
import androidx.room.Transactor
import androidx.room.useWriterConnection

suspend inline fun <T> RoomDatabase.withTransaction(
    type: Transactor.SQLiteTransactionType = Transactor.SQLiteTransactionType.IMMEDIATE,
    crossinline block: suspend () -> T
): T {
    return this.useWriterConnection { transactor ->
        transactor.withTransaction(type) {
            block()
        }
    }
}