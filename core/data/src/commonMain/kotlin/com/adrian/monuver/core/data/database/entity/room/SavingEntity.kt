package com.adrian.monuver.core.data.database.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving")
data class SavingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "target_date") val targetDate: String,
    @ColumnInfo(name = "target_amount") val targetAmount: Long,
    @ColumnInfo(name = "current_amount") val currentAmount: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
)