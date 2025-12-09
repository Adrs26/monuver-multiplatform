package com.adrian.monuver.core.data.mapper

import com.adrian.monuver.core.data.database.entity.room.AccountEntity
import com.adrian.monuver.core.domain.model.Account

fun AccountEntity.toDomain() = Account(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)

fun Account.toEntity() = AccountEntity(
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)

fun Account.toEntityForUpdate() = AccountEntity(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)