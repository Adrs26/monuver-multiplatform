package com.adrian.monuver.core.data.database.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "parent_id") val parentId: Long = 0L,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "due_date") val dueDate: String,
    @ColumnInfo(name = "paid_date") val paidDate: String?,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long,
    @ColumnInfo(name = "amount") val amount: Long,
    @ColumnInfo(name = "is_recurring")val isRecurring: Boolean,
    @ColumnInfo(name = "cycle") val cycle: Int?,
    @ColumnInfo(name = "period") val period: Int?,
    @ColumnInfo(name = "fix_period") val fixPeriod: Int?,
    @ColumnInfo(name = "is_paid") val isPaid: Boolean,
    @ColumnInfo(name = "paid_period") val nowPaidPeriod: Int,
    @ColumnInfo(name = "is_paid_before") val isPaidBefore: Boolean,
)
