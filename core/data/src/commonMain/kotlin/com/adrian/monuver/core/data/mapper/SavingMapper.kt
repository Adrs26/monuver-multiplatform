package com.adrian.monuver.core.data.mapper

import com.adrian.monuver.core.data.database.entity.room.SavingEntity
import com.adrian.monuver.core.domain.model.Saving

fun SavingEntity.toDomain() = Saving(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun Saving.toEntity() = SavingEntity(
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun Saving.toEntityForUpdate() = SavingEntity(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)