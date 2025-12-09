package com.adrian.monuver.core.data.mapper

import com.adrian.monuver.core.data.database.entity.room.BillEntity
import com.adrian.monuver.core.domain.model.Bill

fun BillEntity.toDomain() = Bill(
    id = id,
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)

fun Bill.toEntity() = BillEntity(
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)

fun Bill.toEntityForUpdate() = BillEntity(
    id = id,
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)