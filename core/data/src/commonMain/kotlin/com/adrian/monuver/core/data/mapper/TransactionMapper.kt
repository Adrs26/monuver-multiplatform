package com.adrian.monuver.core.data.mapper

import com.adrian.monuver.core.data.database.entity.projection.TransactionCategoryRecommendationEntity
import com.adrian.monuver.core.data.database.entity.room.TransactionEntity
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)

fun Transaction.toEntity() = TransactionEntity(
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)

fun Transaction.toEntityForUpdate() = TransactionEntity(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)

fun TransactionCategoryRecommendationEntity.toDomain() = TransactionCategoryRecommendation(
    parentCategory = parentCategory,
    childCategory = childCategory
)