package com.adrian.monuver.core.data.database.entity.projection

import androidx.room.ColumnInfo

data class TransactionCategoryRecommendationEntity(
    @ColumnInfo(name = "parent_category") val parentCategory: Int,
    @ColumnInfo(name = "child_category") val childCategory: Int
)
