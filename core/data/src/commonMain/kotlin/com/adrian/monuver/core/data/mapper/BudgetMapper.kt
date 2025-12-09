package com.adrian.monuver.core.data.mapper

import com.adrian.monuver.core.data.database.entity.room.BudgetEntity
import com.adrian.monuver.core.domain.model.Budget

fun BudgetEntity.toDomain() = Budget(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun Budget.toEntity() = BudgetEntity(
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun Budget.toEntityForUpdate() = BudgetEntity(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)