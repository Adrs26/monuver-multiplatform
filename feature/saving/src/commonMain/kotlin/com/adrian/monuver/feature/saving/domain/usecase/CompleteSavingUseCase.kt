package com.adrian.monuver.feature.saving.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CompleteSavingUseCase(
    private val repository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(
        savingId: Long,
        savingName: String,
        savingAmount: Long
    ): DatabaseResultState {
        if (savingAmount == 0L) return DatabaseResultState.EmptySavingAmount

        val currentDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString()
        val (month, year) = DateHelper.getMonthAndYear(currentDate)

        val transaction = Transaction(
            title = "Penyelesaian Tabungan",
            type = TransactionType.EXPENSE,
            parentCategory = TransactionParentCategory.OTHER_EXPENSE,
            childCategory = TransactionChildCategory.SAVINGS_COMPLETE,
            date = currentDate,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = savingAmount,
            sourceId = savingId.toInt(),
            sourceName = savingName,
            isLocked = true,
            isSpecialCase = true
        )

        repository.completeSaving(
            transaction = transaction,
            savingId = savingId
        )
        return DatabaseResultState.CompleteSavingSuccess
    }
}