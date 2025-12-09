package com.adrian.monuver.core.data.database.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "cycle") val cycle: Int,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "max_amount") val maxAmount: Long,
    @ColumnInfo(name = "used_amount") val usedAmount: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "is_overflow_allowed") val isOverflowAllowed: Boolean,
    @ColumnInfo(name = "is_auto_update") val isAutoUpdate: Boolean
)
