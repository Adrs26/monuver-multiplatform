package com.adrian.monuver.core.data.database.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "parent_category") val parentCategory: Int,
    @ColumnInfo(name = "child_category") val childCategory: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "timestamp") val timeStamp: Long,
    @ColumnInfo(name = "amount") val amount: Long,
    @ColumnInfo(name = "source_id") val sourceId: Int,
    @ColumnInfo(name = "source_name") val sourceName: String,
    @ColumnInfo(name = "destination_id") val destinationId: Int? = null,
    @ColumnInfo(name = "destination_name") val destinationName: String? = null,
    @ColumnInfo(name = "save_id") val saveId: Long? = null,
    @ColumnInfo(name = "bill_id") val billId: Long? = null,
    @ColumnInfo(name = "is_locked") val isLocked: Boolean,
    @ColumnInfo(name = "is_special_case") val isSpecialCase: Boolean
)